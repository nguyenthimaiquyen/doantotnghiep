$(document).ready(() => {

    toastr.options.timeOut = 2500; // 2.5s

    let deleteDiscountCodeId = -1;

    function getDiscountCodeUnit() {
        $.ajax({
            url: "/discountCodes/unit",
            type: 'GET',
            contentUnit: "application/json; charset=utf-8",
            success: function (data) {
                const discountCodeUnitSelection = $('#discountCode-modal #discountUnit');
                if (discountCodeUnitSelection.children().length === 0) {
                    if (!data || data.length === 0) {
                        return;
                    }
                    let discountCodeUnitOptions = "";
                    for (let i = 0; i < data.length; i++) {
                        discountCodeUnitOptions += "<option value='" + data[i].code + "'>" + data[i].name + "</option>";
                    }
                    discountCodeUnitSelection.append($(discountCodeUnitOptions));
                }
            },
            error: function (data) {
                console.log("lỗi load dữ liệu")
                toastr.warning(data.responseJSON.error);
            }
        });
    }

    getDiscountCodeUnit();
    setInterval(function () {
        getDiscountCodeUnit();
    }, 900000); // 15p chạy 1 lần

    $.validator.addMethod("discountCustom", function (value, element) {
        if (this.optional(element)) {
            return true;
        }
        return this.optional(element) || value > 0;
    }, "Discount Value must be greater than zero");

    $.validator.addMethod("isNotPastDate", function (value, element) {
        if (this.optional(element)) {
            return true;
        }
        console.log("value : " + value)
        console.log("element : " + element)
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
        console.log("value : " + value)
        console.log("element : " + element)
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
        console.log(myDate - today)
        return this.optional(element) || myDate - today > 0;
    }, "Date must be a future date");

    //validate
    const validator = $('#discountCode-form').validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            'codeName': {
                required: true,
                maxlength: 50
            },
            'discountValue': {
                required: true,
                digits: true,
                discountCustom: true
            },
            "discountUnit": {
                required: true
            },
            "startDate": {
                required: true,
                isNotPastDate: true
            },
            "endDate": {
                required: true,
                futureDate: true
            },
            "usageLimitationCount": {
                required: true,
                digits: true,
                discountCustom: true
            }
        },
        messages: {
            'codeName': {
                required: "Tên mã bắt buộc",
                maxlength: "Tên mã dài tối đa 50 ký tự"
            },
            'discountValue': {
                required: "Giá trị giảm giá bắt buộc",
                digits: "Giá trị giảm giá là số",
                discountCustom: "Giá trị giảm giá lớn hơn 0"
            },
            "discountUnit": {
                required: "Đơn vị giảm giá bắt buộc"
            },
            "startDate": {
                required: "Ngày hiệu lực bắt buộc",
                isNotPastDate: "Ngày hiệu lực không là ngày quá khứ"
            },
            "endDate": {
                required: "Ngày hết hiệu lực bắt buộc",
                futureDate: "Ngày hết hiệu lực là ngày tương lai"
            },
            "usageLimitationCount": {
                required: "Giới hạn số lần sử dụng bắt buộc",
                digits: "Giới hạn số lần sử dụng là số",
                discountCustom: "Giới hạn số lần sử dụng lớn hơn 0"
            }
        }
    });

    //open modal to create a new discountCode
    $('.create-discountCode-btn').click(function () {
        $('#discountCode-modal #save-discountCode-btn').attr("action-type", "CREATE");
        $('#discountCode-modal').modal('show');
    });

    //open modal to update a discountCode
    $('.update-discountCode-btn').click(async function (event) {
        //call api lên java để lấy dữ liệu
        const updateDiscountCodeId = parseInt($(event.currentTarget).attr("discountCode-id"));
        let discountCode = null;
        await $.ajax({
            url: "/discountCodes/" + updateDiscountCodeId,
            type: "GET",
            success: function (data) {
                discountCode = data;
            },
            error: function (err) {
                toastr.warning(data.responseJSON.error);
                toastr.warning("Đã có lỗi xảy ra, vui lòng thử lại!");
            }
        });

        if (!discountCode) {
            toastr.error("Đã có lỗi xảy ra, vui lòng thử lại!")
            return;
        }
        console.log(discountCode)
        const startDate = discountCode.startDate[0] + "-" + discountCode.startDate[1] + '-' + discountCode.startDate[2];
        const endDate = discountCode.endDate[0] + "-" + discountCode.endDate[1] + '-' + discountCode.endDate[2];
        console.log(startDate)
        console.log(endDate)
        //đổ dữ liệu vào form
        $('#discountCode-form #codeName').val(discountCode.codeName);
        $('#discountCode-form #discountValue').val(discountCode.discountValue);
        $('#discountCode-form #discountUnit').val(discountCode.discountUnit);
        $('#discountCode-form #startDate').val(startDate);
        $('#discountCode-form #endDate').val(endDate);
        $('#discountCode-form #usageLimitationCount').val(discountCode.usageLimitationCount);

        $('#discountCode-modal #save-discountCode-btn').attr('action-type', "UPDATE");
        $('#discountCode-modal #save-discountCode-btn').attr("discountCode-id", updateDiscountCodeId);
        $('#discountCode-modal').modal("show");
    });

    //create or update a discountCode
    $('#save-discountCode-btn').click(function (event) {
        //validate
        const isValidForm = $('#discountCode-form').valid();
        if (!isValidForm) {
            return;
        }
        const actionType = $(event.currentTarget).attr("action-type");
        const discountCodeId = $(event.currentTarget).attr("discountCode-id");
        //lấy dữ liệu từ form
        const formDiscountCodeData = $('#discountCode-form').serializeArray();
        if (!formDiscountCodeData || formDiscountCodeData.length === 0) {
            return;
        }
        //chuyển dữ liệu từ object sang json
        const discountCodeRequestBody = {};
        for (let i = 0; i < formDiscountCodeData.length; i++) {
            discountCodeRequestBody[formDiscountCodeData[i].name] = formDiscountCodeData[i].value;
        }
        const method = actionType === "CREATE" ? "POST" : "PUT";
        if (method === "PUT") {
            discountCodeRequestBody["id"] = discountCodeId;
        }
        //call api lên backend
        $.ajax({
            url: "/discountCodes",
            type: method,
            data: JSON.stringify(discountCodeRequestBody),
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                toastr.success((method === "CREATE" ? "Tạo mới " : "Cập nhật ") + "thành công mã giảm giá!");
                setTimeout(() => {
                    location.reload();
                }, 1000);
            },
            error: function (error) {
                toastr.warning("Đã có lỗi xảy ra, vui lòng thử lại!");
            }
        });
        $("#discountCode-modal #save-discountCode-btn").attr("action-type", "");
        $('#discountCode-modal #save-discountCode-btn').attr("discountCode-id", "");
    });

    //show modal to delete a discountCode
    $('.delete-discountCode-btn').click(function (event) {
        deleteDiscountCodeId = parseInt($(event.currentTarget).attr("discountCode-id"));
        $('#discountCode-delete-modal').modal('show');
    });

    //delete a discountCode
    $('#delete-discountCode-btn').click(function () {
        $.ajax({
            url: "/discountCodes/" + deleteDiscountCodeId,
            type: "DELETE",
            success: function (data) {
                toastr.success("Xóa mã giảm giá thành công!");
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
    $('#discountCode-modal').on('hidden.bs.modal', function () {
        $("#discountCode-modal #save-discountCode-btn").attr("action-type", "");
        $('#discountCode-modal #save-discountCode-btn').attr("discountCode-id", "");
        $('#discountCode-form').trigger("reset");
        $('#discountCode-form input').removeClass("error");
        validator.resetForm();
    });



});