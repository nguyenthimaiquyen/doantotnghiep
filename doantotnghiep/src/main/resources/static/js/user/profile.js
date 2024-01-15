$(document).ready(() => {

    toastr.options.timeOut = 2500; // 2.5s

    //hiển thị tên người dùng khi chỉnh sửa hồ sơ
    const userInfo = JSON.parse(localStorage.getItem('user-info'));
    const fullName = userInfo ? userInfo.fullName : null;

    if (fullName) {
        $('#profile-fullName').val(fullName);
    }

    //thực hiện chức năng đổi mật khẩu
    $.validator.addMethod("passwordFormat", function (value, element) {
        return this.optional(element) || /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/i.test(value);
    }, "Mật khẩu tối thiểu 8 ký tự, ít nhất một chữ cái, một số và một ký tự đặc biệt");

    //validate form đổi mật khẩu
    const changePasswordValidator = $('#change-password-form').validate({
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
            url: "/profile/changePassword",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(RequestBody),
            success: function (data) {
                toastr.success("Thay đổi mật khẩu thành công!");
                console.log(data);
            },
            error: function (error) {
                toastr.error("Đã có lỗi xảy ra, vui lòng thử lại sau!");
                console.log(error);
            }
        });
    });

    //thực hiện chức năng quên mật khẩu
    //validate form gửi otp
    const otpSendingValidator = $('#otp-sending-form').validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            'email': {
                required: true,
                emailFormat: true,
                maxlength: 50
            }
        },
        messages: {
            'email': {
                required: "Email bắt buộc",
                emailFormat: "Vui lòng nhập địa chỉ email hợp lệ",
                maxlength: "Email dài tối đa 50 ký tự"
            }
        }
    });

    //gọi api gửi OTP
    $('#otp-sending-btn').click(async () => {
        //validate
        const isValidForm = $('#otp-sending-form').valid();
        if (!isValidForm) {
            return;
        }
        //lấy dữ liệu từ form
        const emailData = $('#otp-sending-form').serializeArray();
        if (!emailData || emailData.length === 0) {
            return;
        }
        console.log(emailData)
        //chuyển dữ liệu từ object sang json
        const RequestBody = {};
        for (let i = 0; i < emailData.length; i++) {
            RequestBody[emailData[i].name] = emailData[i].value;
        }
        console.log(RequestBody)
        //call api lên backend
        await $.ajax({
            type: "POST",
            url: "/profile/otp-sending",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(RequestBody),
            success: function (data) {
                toastr.success("Vui lòng kiểm tra email để lấy OTP!");
                console.log(data);
                setTimeout(() => {
                    window.location.href = "http://localhost:8080/profile/forget-password";
                }, 5000);
            },
            error: function (error) {
                toastr.error("Đã có lỗi xảy ra, vui lòng thử lại sau!");
                console.log(error);
            }
        });
    });

    //validate form quên mật khẩu
    const forgetPasswordValidator = $('#forget-password-form').validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            'otp': {
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
            'otp': {
                required: "OTP bắt buộc"
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

    //gọi api quên mật khẩu
    $('#forget-password-btn').click(async () => {
        //validate
        const isValidForm = $('#forget-password-form').valid();
        if (!isValidForm) {
            return;
        }
        //lấy dữ liệu từ form
        const forgetPasswordData = $('#forget-password-form').serializeArray();
        if (!forgetPasswordData || forgetPasswordData.length === 0) {
            return;
        }
        console.log(forgetPasswordData)
        //chuyển dữ liệu từ object sang json
        const RequestBody = {};
        for (let i = 0; i < forgetPasswordData.length; i++) {
            RequestBody[forgetPasswordData[i].name] = forgetPasswordData[i].value;
        }
        console.log(RequestBody)
        //call api lên backend
        await $.ajax({
            type: "PUT",
            url: "/profile/forgetPassword",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(RequestBody),
            success: function (data) {
                toastr.success("Đặt lại mật khẩu thành công!");
                console.log(data);
                setTimeout(() => {
                    window.location.href = "http://localhost:8080";
                }, 3000);
            },
            error: function (error) {
                toastr.error("Đã có lỗi xảy ra, vui lòng thử lại sau!");
                console.log(error);
            }
        });
    });

});