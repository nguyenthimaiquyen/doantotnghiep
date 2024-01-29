$(document).ready(() => {


    let deleteSectionId = -1;

    //2. chức năng thêm sửa xóa section

    //validate form thông tin chung của khóa học
    const sectionValidator = $('#section-form').validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            'title': {
                required: true,
                maxlength: 255
            }
        },
        messages: {
            'title': {
                required: "Tiêu đề bắt buộc",
                maxlength: "Tiêu đề dài tối đa 255 ký tự"
            }
        }
    });

    //mở modal khi ấn thêm chương
    $('.create-section-btn').click(() => {
        $('#section-modal #save-section-btn').attr("action-type", "CREATE");
        $('#section-modal').modal('show');
    });

    //mở modal khi ấn cập nhật chương
    $('.update-section-btn').click(async function (event) {
        //call api lên java để lấy dữ liệu
        const updateSectionId = parseInt($(event.currentTarget).attr("section-id"));
        let section = null;
        await $.ajax({
            url: "/api/v1/sections/" + updateSectionId,
            type: "GET",
            success: function (data) {
                section = data;
            },
            error: function (err) {
                console.log(err)
                $.toast({
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    position: 'top-right',
                    bgColor: '#FF0000'
                })
            }
        });

        if (!section) {
            $.toast({
                text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                icon: 'error',
                position: 'top-right',
                bgColor: '#FF0000'
            })
            return;
        }

        //đổ dữ liệu vào form
        $('#section-form #title').val(section.title);

        $('#section-modal #save-section-btn').attr('action-type', "UPDATE");
        $('#section-modal #save-section-btn').attr("section-id", updateSectionId);
        $('#section-modal').modal("show");
    });

    //create or update a section
    $('#save-section-btn').click(function (event) {
        //validate
        const isValidForm = $('#section-form').valid();
        if (!isValidForm) {
            return;
        }
        const actionType = $(event.currentTarget).attr("action-type");
        const sectionId = $(event.currentTarget).attr("section-id");
        //lấy dữ liệu từ form
        const formSectionData = $('#section-form').serializeArray();
        if (!formSectionData || formSectionData.length === 0) {
            return;
        }
        //chuyển dữ liệu từ object sang json
        const sectionRequestBody = {};
        for (let i = 0; i < formSectionData.length; i++) {
            sectionRequestBody[formSectionData[i].name] = formSectionData[i].value;
        }
        const method = actionType === "CREATE" ? "POST" : "PUT";
        if (method === "PUT") {
            sectionRequestBody["id"] = sectionId;
        }
        const courseId = localStorage.getItem('course-id');
        sectionRequestBody["courseId"] = courseId;
        //call api lên backend
        $.ajax({
            url: "/api/v1/sections",
            type: method,
            data: JSON.stringify(sectionRequestBody),
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                $.toast({
                    text: (method === "POST" ? "Tạo mới " : "Cập nhật ") + "thành công chương của khóa học!",
                    icon: 'success',
                    position: 'top-right',
                    bgColor: '#4CAF50'
                })
                setTimeout(() => {
                    location.reload();
                }, 3000);
            },
            error: function (error) {
                $.toast({
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    position: 'top-right',
                    bgColor: '#FF0000'
                })
            }
        });
        $("#section-modal #save-section-btn").attr("action-type", "");
        $('#section-modal #save-section-btn').attr("section-id", "");
    });

    //show modal to delete a section
    $('.delete-section-btn').click(function (event) {
        deleteSectionId = parseInt($(event.currentTarget).attr("section-id"));
        $('#section-delete-modal').modal('show');
    });

    //delete a section
    $('#delete-section-btn').click(function () {
        $.ajax({
            url: "/api/v1/sections/" + deleteSectionId,
            type: "DELETE",
            success: function (data) {
                $.toast({
                    text: 'Xóa chương thành công!',
                    icon: 'success',
                    position: 'top-right',
                    bgColor: '#4CAF50'
                })
                setTimeout(() => {
                    location.reload();
                }, 3000);
            },
            error: function (err) {
                console.log(err)
                $.toast({
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    position: 'top-right',
                    bgColor: '#FF0000'
                })
            }
        });
    });

    //reset form
    $('#section-modal').on('hidden.bs.modal', function () {
        $("#section-modal #save-section-btn").attr("action-type", "");
        $('#section-modal #save-section-btn').attr("section-id", "");
        $('#section-form').trigger("reset");
        $('#section-form input').removeClass("error");
        sectionValidator.resetForm();
    });

    //xử lý icon khi nhấn vào tên chương
    $('.sectionTitle').click(() => {

    });


});




