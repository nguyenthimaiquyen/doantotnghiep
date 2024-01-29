$(document).ready(() => {


    //hiển thị tên người dùng khi chỉnh sửa hồ sơ
    const userInfo = JSON.parse(localStorage.getItem('user-info'));
    const fullName = userInfo ? userInfo.fullName : null;
    const role = userInfo ? userInfo.roles : null;

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
        //chuyển dữ liệu từ object sang json
        const RequestBody = {};
        for (let i = 0; i < changePasswordData.length; i++) {
            RequestBody[changePasswordData[i].name] = changePasswordData[i].value;
        }
        RequestBody["email"] = email;
        //call api lên backend
        await $.ajax({
            type: "PUT",
            url: "/api/v1/profile/change-password",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(RequestBody),
            success: function (data) {
                $.toast({
                    heading: 'Thành công',
                    text: 'Thay đổi mật khẩu thành công!',
                    icon: 'success',
                    showHideTransition: 'fade',
                    position: 'top-right',
                    loader: false,
                    bgColor: '#4CAF50'
                })
            },
            error: function (error) {
                $.toast({
                    heading: 'Lỗi',
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    showHideTransition: 'fade',
                    position: 'top-right',
                    loader: false,
                    bgColor: '#FF0000'
                })
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
        //chuyển dữ liệu từ object sang json
        const RequestBody = {};
        for (let i = 0; i < emailData.length; i++) {
            RequestBody[emailData[i].name] = emailData[i].value;
        }
        //call api lên backend
        await $.ajax({
            type: "POST",
            url: "/api/v1/profile/otp-sending",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(RequestBody),
            success: function (data) {
                $.toast({
                    text: 'Vui lòng kiểm tra email để lấy OTP!',
                    icon: 'success',
                    position: 'top-right',
                    bgColor: '#4CAF50'
                })
                setTimeout(() => {
                    window.location.href = "http://localhost:8080/profile/forget-password";
                }, 5000);
            },
            error: function (error) {
                $.toast({
                    heading: 'Lỗi',
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    showHideTransition: 'fade',
                    position: 'top-right',
                    loader: false,
                    bgColor: '#FF0000'
                })
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
        //chuyển dữ liệu từ object sang json
        const RequestBody = {};
        for (let i = 0; i < forgetPasswordData.length; i++) {
            RequestBody[forgetPasswordData[i].name] = forgetPasswordData[i].value;
        }
        //call api lên backend
        await $.ajax({
            type: "PUT",
            url: "/api/v1/profile/forget-password",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(RequestBody),
            success: function (data) {
                $.toast({
                    text: 'Đặt lại mật khẩu thành công!',
                    icon: 'success',
                    position: 'top-right',
                    bgColor: '#4CAF50'
                })
                setTimeout(() => {
                    window.location.href = "http://localhost:8080";
                }, 3000);
            },
            error: function (error) {
                $.toast({
                    heading: 'Lỗi',
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    showHideTransition: 'fade',
                    position: 'top-right',
                    loader: false,
                    bgColor: '#FF0000'
                })
                console.log(error);
            }
        });
    });

    if (role == "TEACHER") {
        let teacherData = `
                                                    <div class="col-xl-6  col-lg-6  col-md-6  col-sm-6 col-12 my-2">
                                                        <div class="checkout-form-list mb-3">
                                                            <label for="profile-expertise">Chuyên ngành giảng dạy</label>
                                                            <input type="text" placeholder="Nhập chuyên ngành giảng dạy"
                                                                   class="form-control w-100"
                                                                   id="profile-expertise" name="profile-expertise">
                                                        </div>
                                                    </div>
                                                    <div class="col-xl-6  col-lg-6  col-md-6  col-sm-6 col-12 my-2">
                                                        <div class="checkout-form-list mb-3">
                                                            <label for="profile-experience">Số năm kinh nghiệm</label>
                                                            <input type="number" placeholder="Nhập số năm kinh nghiệm"
                                                                   class="form-control w-100"
                                                                   id="profile-experience" name="profile-experience">
                                                        </div>
                                                    </div>
                                                    <div class="col-xl-6  col-lg-6  col-md-6  col-sm-6 col-12 my-2">
                                                        <div class="custom-file pmd-custom-file-filled my-2">
                                                            <input type="file" class="custom-file-input" id="profile-degree">
                                                            <label class="custom-file-label" for="profile-avatar">Tải bằng đại học</label>
                                                        </div>
                                                    </div>
                                                    <div class="col-xl-6  col-lg-6  col-md-6  col-sm-6 col-12 my-2">
                                                        <div class="custom-file pmd-custom-file-filled my-2">
                                                            <input type="file" class="custom-file-input" id="profile-certification">
                                                            <label class="custom-file-label" for="profile-avatar">Tải chứng chỉ</label>
                                                        </div>
                                                    </div>                                                   
        `;
        $('#profile-data').append(teacherData);
    }

});