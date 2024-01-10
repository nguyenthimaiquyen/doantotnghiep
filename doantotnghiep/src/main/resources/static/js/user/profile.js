$(document).ready(() => {

    toastr.options.timeOut = 2500; // 2.5s

    //hiển thị tên người dùng khi chỉnh sửa hồ sơ
    const userInfo = JSON.parse(localStorage.getItem('user-info'));
    const fullName = userInfo ? userInfo.fullName : null;
    const email = userInfo ? userInfo.email : null;

    if (fullName) {
        $('#profile-fullName').val(fullName);
    }

    //thực hiện chức năng đổi mật khẩu
    $.validator.addMethod("passwordFormat", function (value, element) {
        return this.optional(element) || /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/i.test(value);
    }, "Mật khẩu tối thiểu 8 ký tự, ít nhất một chữ cái, một số và một ký tự đặc biệt");

    //validate form đổi mật khẩu
    const validator = $('#change-password-form').validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            'oldPassword': {
                required: true
            },
            'newPassword': {
                required: true,
                passwordFormat: true
            },
            "renewPassword": {
                required: true,
                equalTo: '#newPassword'
            }
        },
        messages: {
            'oldPassword': {
                required: "Mật khẩu bắt buộc"
            },
            'newPassword': {
                required: "Mật khẩu mới bắt buộc",
                passwordFormat: "Mật khẩu tối thiểu 8 ký tự, ít nhất một chữ cái, một số và một ký tự đặc biệt"
            },
            "renewPassword": {
                required: "Nhập lại mật khẩu bắt buộc",
                equalTo: "Mật khẩu nhập lại phải khớp với mật khẩu mới"
            }
        }
    });

    //gọi api đổi mật khẩu
    $('#change-password').click(async () => {
        //validate
        const isValidForm = $('#change-password-form').valid();
        if (!isValidForm) {
            return;
        }
        //lấy dữ liệu từ form
        const changePasswordData = $('#change-password-form').serializeArray();
        if (!changePasswordData || changePasswordData.length === 0) {
            return;
        }
        console.log(changePasswordData)
        //chuyển dữ liệu từ object sang json
        const RequestBody = {};
        for (let i = 0; i < changePasswordData.length; i++) {
            RequestBody[changePasswordData[i].name] = changePasswordData[i].value;
        }
        RequestBody["email"] = email;
        console.log(RequestBody)
        //call api lên backend
        await $.ajax({
            type: "PUT",
            url: "/profiles/changePassword",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(RequestBody),
            success: function (data) {
                toastr.success("Thay đổi mật khẩu thành công!");
                // setTimeout(() => {
                //     location.reload();
                // }, 1000);
                console.log(data);
            },
            error: function (error) {
                toastr.error("Đã có lỗi xảy ra, vui lòng thử lại sau!");
                console.log(error);
            }
        });
    });

    //

});