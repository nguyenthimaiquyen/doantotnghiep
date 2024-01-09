$(document).ready(() => {

    toastr.options.timeOut = 2500; // 2.5s

    let deleteReminderId = -1;

    function getCourses() {
        $.ajax({
            url: "/reminders/courses",
            type: 'GET',
            contentUnit: "application/json; charset=utf-8",
            success: function (data) {
                const courseSelection = $('#reminder-modal #courseID');
                if (courseSelection.children().length === 0) {
                    if (!data || data.length === 0) {
                        return;
                    }
                    let courseOptions = "";
                    for (let i = 0; i < data.length; i++) {
                        courseOptions += "<option value='" + data[i].id + "'>" + data[i].title + "</option>";
                    }
                    courseSelection.append($(courseOptions));
                }
            },
            error: function (data) {
                console.log("lỗi load dữ liệu")
                toastr.warning(data.responseJSON.error);
            }
        });
    }

    function getFrequency() {
        $.ajax({
            url: "/reminders/frequency",
            type: 'GET',
            contentUnit: "application/json; charset=utf-8",
            success: function (data) {
                const frequencySelection = $('#reminder-modal #frequency');
                if (frequencySelection.children().length === 0) {
                    if (!data || data.length === 0) {
                        return;
                    }
                    let frequencyOptions = "";
                    for (let i = 0; i < data.length; i++) {
                        frequencyOptions += "<option value='" + data[i].code + "'>" + data[i].name + "</option>";
                    }
                    frequencySelection.append($(frequencyOptions));
                }
            },
            error: function (data) {
                console.log("lỗi load dữ liệu")
                toastr.warning(data.responseJSON.error);
            }
        });
    }

    getCourses();
    getFrequency();
    setInterval(function () {
        getFrequency();
        getCourses();
    }, 900000); // 15p chạy 1 lần

    $.validator.addMethod("isNotPastDate", function (value, element) {
        if (this.optional(element)) {
            return true;
        }
        const myDate = new Date(value);
        myDate.setHours(0);
        myDate.setMinutes(0);
        myDate.setSeconds(0);
        myDate.setMilliseconds(0);
        const today = new Date();
        today.setHours(0);
        today.setMinutes(0);
        today.setSeconds(0);
        today.setMilliseconds(0);
        return this.optional(element) || myDate - today > 0 || myDate === today;
    }, "Date must be a future date");

    $.validator.addMethod("futureDate", function (value, element) {
        if (this.optional(element)) {
            return true;
        }
        const myDate = new Date(value);
        myDate.setHours(0);
        myDate.setMinutes(0);
        myDate.setSeconds(0);
        myDate.setMilliseconds(0);
        const today = new Date();
        today.setHours(0);
        today.setMinutes(0);
        today.setSeconds(0);
        today.setMilliseconds(0);
        return this.optional(element) || myDate - today > 0;
    }, "Date must be a future date");

    //validate
    const validator = $('#reminder-form').validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            'eventName': {
                maxlength: 100
            },
            'courseID': {
                required: true
            },
            "frequency": {
                required: true
            },
            "time": {
                required: true
            },
            "startDate": {
                required: true,
                isNotPastDate: true
            },
            "endDate": {
                required: true,
                futureDate: true
            }
        },
        messages: {
            'eventName': {
                maxlength: "Tên sự kiện dài tối đa 100 ký tự"
            },
            'courseID': {
                required: "Khóa học bắt buộc"
            },
            "frequency": {
                required: "Tần suất nhắc nhở bắt buộc"
            },
            "time": {
                required: "Thời gian nhắc nhở bắt buộc"
            },
            "startDate": {
                required: "Ngày bắt đầu bắt buộc",
                isNotPastDate: "Ngày bắt đầu không là ngày quá khứ"
            },
            "endDate": {
                required: "Ngày kết thúc bắt buộc",
                futureDate: "Ngày kết thúc là ngày tương lai"
            }
        }
    });

    //open modal to create a new reminder
    $('.create-reminder-btn').click(function () {
        $('#reminder-modal #save-reminder-btn').attr("action-type", "CREATE");
        $('#reminder-modal').modal('show');
    });

    //open modal to update a reminder
    $('.update-reminder-btn').click(async function (event) {
        //call api lên java để lấy dữ liệu
        const updateReminderId = parseInt($(event.currentTarget).attr("reminder-id"));
        let reminder = null;
        await $.ajax({
            url: "/reminders/" + updateReminderId,
            type: "GET",
            success: function (data) {
                reminder = data;
            },
            error: function (err) {
                toastr.warning(data.responseJSON.error);
                toastr.warning("Đã có lỗi xảy ra, vui lòng thử lại!");
            }
        });

        if (!reminder) {
            toastr.error("Đã có lỗi xảy ra, vui lòng thử lại!")
            return;
        }

        //đổ dữ liệu vào form
        $('#reminder-form #eventName').val(reminder.eventName);
        $('#reminder-form #courseID').val(reminder.courseID); //check lại
        $('#reminder-form #frequency').val(reminder.frequency);
        $('#reminder-form #time').val(reminder.time);
        $('#reminder-form #startDate').val(reminder.startDate);
        $('#reminder-form #endDate').val(reminder.endDate);

        $('#reminder-modal #save-reminder-btn').attr('action-type', "UPDATE");
        $('#reminder-modal #save-reminder-btn').attr("reminder-id", updateReminderId);
        $('#reminder-modal').modal("show");
    });

    //create or update a reminder
    $('#save-reminder-btn').click(function (event) {
        //validate
        const isValidForm = $('#reminder-form').valid();
        if (!isValidForm) {
            return;
        }
        const actionType = $(event.currentTarget).attr("action-type");
        const reminderId = $(event.currentTarget).attr("reminder-id");
        //lấy dữ liệu từ form
        const formReminderData = $('#reminder-form').serializeArray();
        if (!formReminderData || formReminderData.length === 0) {
            return;
        }
        //chuyển dữ liệu từ object sang json
        const reminderRequestBody = {};
        for (let i = 0; i < formReminderData.length; i++) {
            reminderRequestBody[formReminderData[i].name] = formReminderData[i].value;
        }
        const method = actionType === "CREATE" ? "POST" : "PUT";
        if (method === "PUT") {
            reminderRequestBody["id"] = reminderId;
        }
        //call api lên backend
        $.ajax({
            url: "/reminders",
            type: method,
            data: JSON.stringify(reminderRequestBody),
            contentUnit: "application/json; charset=utf-8",
            success: function (data) {
                toastr.success((method === "CREATE" ? "Tạo mới " : "Cập nhật ") + "thành công sự kiện nhắc nhở học tập!");
                setTimeout(() => {
                    location.reload();
                }, 1000);
            },
            error: function (error) {
                toastr.warning("Đã có lỗi xảy ra, vui lòng thử lại!");
            }
        });
        $("#reminder-modal #save-reminder-btn").attr("action-type", "");
        $('#reminder-modal #save-reminder-btn').attr("reminder-id", "");
    });

    //show modal to delete a reminder
    $('.delete-reminder-btn').click(function (event) {
        deleteReminderId = parseInt($(event.currentTarget).attr("reminder-id"));
        $('#reminder-delete-modal').modal('show');
    });

    //delete a reminder
    $('#delete-reminder-btn').click(function () {
        $.ajax({
            url: "/reminders/" + deleteReminderId,
            type: "DELETE",
            success: function (data) {
                toastr.success("Xóa sự kiện nhắc nhở học tập thành công!");
                setTimeout(() => {
                    location.reload();
                }, 1000);
            },
            error: function (err) {
                toastr.warning("Đã có lỗi xảy ra, vui lòng thử lại!");
            }
        });
    });

    //reset form
    $('#reminder-modal').on('hidden.bs.modal', function () {
        $("#reminder-modal #save-reminder-btn").attr("action-type", "");
        $('#reminder-modal #save-reminder-btn').attr("reminder-id", "");
        $('#reminder-form').trigger("reset");
        $('#reminder-form input').removeClass("error");
        validator.resetForm();
    });

    //xử lý sự kiện click vào dấu 3 chấm để hiển thị danh sách các lựa chọn
    const showMoreReminder = $('#show-more-reminder');
    const reminderMenu = $('.dropdown-menu');
    showMoreReminder.click(() => {
        reminderMenu.toggle();
    });
    //tắt menu khi ấn ra ngoài
    $(document).on('click', function (event) {
        if (!reminderMenu.is(event.target) && reminderMenu.has(event.target).length === 0 && !showMoreReminder.is(event.target)) {
            reminderMenu.hide();
        }
    });

});