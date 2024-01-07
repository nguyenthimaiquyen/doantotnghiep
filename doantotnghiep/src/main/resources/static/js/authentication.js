$(document).ready(function () {

    toastr.options.timeOut = 2500; // 2.5s

    //validate form đăng nhập
    $.validator.addMethod("emailFormat", function (value, element) {
        return this.optional(element) || /^[A-Za-z0-9+_.-]+@(.+)$/i.test(value);
    }, "Vui lòng nhập email hợp lệ");

    $.validator.addMethod("passwordFormat", function (value, element) {
        return this.optional(element) || /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/i.test(value);
    }, "Mật khẩu tối thiểu 8 ký tự, ít nhất một chữ cái, một số và một ký tự đặc biệt");

    const loginValidator = $('#login-form').validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            'email': {
                required: true,
                emailFormat: true,
                maxlength: 50
            },
            'password': {
                required: true,
                passwordFormat: true
            }
        },
        messages: {
            'email': {
                required: "Email bắt buộc",
                emailFormat: "Vui lòng nhập địa chỉ email hợp lệ",
                maxlength: "Email dài tối đa 50 ký tự"
            },
            'password': {
                required: "Mật khẩu bắt buộc",
                passwordFormat: "Mật khẩu tối thiểu 8 ký tự, ít nhất một chữ cái, một số và một ký tự đặc biệt"
            }
        }
    });

    //gọi api đăng nhập
    $('#login-btn').click(async function (event) {
        //validate
        const isValidForm = $('#login-form').valid();
        if (!isValidForm) {
            return;
        }
        //lấy dữ liệu từ form
        const loginData = $('#login-form').serializeArray();
        if (!loginData || loginData.length === 0) {
            return;
        }
        console.log(loginData)
        //chuyển dữ liệu từ object sang json
        const RequestBody = {};
        for (let i = 0; i < loginData.length; i++) {
            RequestBody[loginData[i].name] = loginData[i].value;
        }
        console.log(RequestBody)
        //call api lên backend
        await $.ajax({
            type: "POST",
            url: "/api/v1/authentication/login",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(RequestBody),
            success: function (data) {
                toastr.success("Đăng nhập thành công!");
                console.log(data);
                localStorage.setItem("access-token", data.jwt);
                localStorage.setItem("refresh-token", data.refreshToken);
                console.log("vào hàm rồi")
                const userInfo = {
                    role: data.role,
                    email: data.username,
                    fullName: data.fullName,
                    avatar: data.avatar
                };
                console.log(data.username)
                localStorage.setItem("user-info", JSON.stringify(userInfo));
                window.location.href = "http://localhost:8080/api/v1";
            },
            error: function (error) {
                toastr.error("Đã có lỗi xảy ra, vui lòng thử lại sau!");
                console.log(error);
            }
        });

        //hiển thị thông tin user lên header
        const userInfo = JSON.parse(localStorage.getItem("user-info"));
        console.log(userInfo)
        if (userInfo) {
            const fullName = userInfo.fullName;
            const email = userInfo.email;
            const avatar = userInfo.avatar;

            $('#user-fullName').innerText = fullName;
            $('#user-avatar').src = avatar;
            $('#user-email').innerText = email;
        }

    });

    //validate form đăng ký
    const signupValidator = $('#signup-form').validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            'fullName': {
                required: true,
                maxlength: 100
            },
            'email': {
                required: true,
                emailFormat: true,
                maxlength: 50
            },
            'password': {
                required: true,
                passwordFormat: true
            }
        },
        messages: {
            'fullName': {
                required: "Họ và tên bắt buộc",
                maxlength: "Họ và tên dài tối đa 50 ký tự"
            },
            'email': {
                required: "Email bắt buộc",
                emailFormat: "Vui lòng nhập địa chỉ email hợp lệ",
                maxlength: "Email dài tối đa 50 ký tự"
            },
            'password': {
                required: "Mật khẩu bắt buộc",
                passwordFormat: "Mật khẩu tối thiểu 8 ký tự, ít nhất một chữ cái, một số và một ký tự đặc biệt"
            }
        }
    });

    //gọi api đăng ký
    $('#signup-btn').click(function (event) {
        //validate
        const isValidForm = $('#signup-form').valid();
        if (!isValidForm) {
            return;
        }
        //lấy dữ liệu từ form
        const signupData = $('#signup-form').serializeArray();
        if (!signupData || signupData.length === 0) {
            return;
        }
        //chuyển dữ liệu từ object sang json
        const RequestBody = {};
        for (let i = 0; i < signupData.length; i++) {
            RequestBody[signupData[i].name] = signupData[i].value;
        }
        //call api lên backend
        $.ajax({
            type: "POST",
            url: "/api/v1/authentication/signup",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(RequestBody),
            success: function (data) {
                toastr.success("Đăng ký tài khoản thành công!");
                console.log(data);
            },
            error: function (error) {
                toastr.error("Đã có lỗi xảy ra, vui lòng thử lại sau!");
                console.log(error);
            }
        });
    });

    //xử lý sự kiện click vào avatar để hiển thị danh sách các lựa chọn
    const userAvatar = $('#user-avatar');
    const userMenu = $('.dropdown-menu');
    userAvatar.click(() => {
        userMenu.toggle();
    });
    //tắt menu khi ấn ra ngoài
    $(document).on('click', function (event) {
        if (!userMenu.is(event.target) && userMenu.has(event.target).length === 0 && !userAvatar.is(event.target)) {
            userMenu.hide();
        }
    });

    //gọi api đăng xuất
    $('.dropdown-menu').on('click', '.logout-link', function (event) {
        event.preventDefault();

        // Gọi API logout và xử lý kết quả
        $.ajax({
            type: 'POST',
            url: '/api/v1/authentication/logout',
            success: function () {
                localStorage.removeItem('access-token');
                localStorage.removeItem('refresh-token');
                localStorage.removeItem('user-info');
                window.location.href = "http://localhost:8080/api/v1";
            },
            error: function (error) {
                console.log(error);
                alert('Đã có lỗi xảy ra khi đăng xuất. Vui lòng thử lại sau.');
            }
        });
    });



});