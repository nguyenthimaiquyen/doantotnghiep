$(document).ready(() => {

    toastr.options.timeOut = 2500; // 2.5s

    let deleteCourseId = -1;

    //2. chức năng thêm sửa xóa section

    //ấn thêm chương thì hiện modal
    $('.create-section-btn').click(() => {
        $('#section-creation-modal #save-section-btn').attr("action-type", "CREATE");
        $('#section-creation-modal').modal('show');
    });

    //open modal to update a discountCode
    $('.update-discountCode-btn').click(async function (event) {
        //call api lên java để lấy dữ liệu
        const updateDiscountCodeId = parseInt($(event.currentTarget).attr("discountCode-id"));
        let discountCode = null;
        await $.ajax({
            url: "/api/v1/discount-codes/" + updateDiscountCodeId,
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

        //đổ dữ liệu vào form
        $('#discountCode-form #codeName').val(discountCode.codeName);
        $('#discountCode-form #discountValue').val(discountCode.discountValue);
        $('#discountCode-form #discountUnit').val(discountCode.discountUnit);
        $('#discountCode-form #startDate').val(discountCode.startDate);
        $('#discountCode-form #endDate').val(discountCode.endDate);
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
            url: "/api/v1/discount-codes",
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
            url: "/api/v1/discount-codes/" + deleteDiscountCodeId,
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