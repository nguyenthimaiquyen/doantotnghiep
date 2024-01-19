$(document).ready(function () {


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
                passwordFormat: true,
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
                passwordFormat: "Mật khẩu tối thiểu 8 ký tự, ít nhất một chữ cái, một số và một ký tự đặc biệt",
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
        //chuyển dữ liệu từ object sang json
        const RequestBody = {};
        for (let i = 0; i < loginData.length; i++) {
            RequestBody[loginData[i].name] = loginData[i].value;
        }
        //call api lên backend
        await $.ajax({
            type: "POST",
            url: "/api/v1/authentication/login",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(RequestBody),
            success: function (data) {
                $.toast({
                    heading: 'Thành công',
                    text: 'Đăng nhập thành công.',
                    icon: 'success',
                    showHideTransition: 'fade',
                    position: 'top-right',
                    loader: false,
                    bgColor: '#4CAF50'
                })
                localStorage.setItem("access-token", data.jwt);
                localStorage.setItem("refresh-token", data.refreshToken);
                const userInfo = {
                    roles: data.roles,
                    email: data.email,
                    fullName: data.fullName,
                    avatar: data.avatar
                };
                localStorage.setItem("user-info", JSON.stringify(userInfo));
                setTimeout(() => {
                    if (data.roles == 'ADMIN') {
                        window.location.href = "http://localhost:8080/courses/analysis/admin";
                    } else if (data.roles == 'TEACHER') {
                        window.location.href = "http://localhost:8080/courses/analysis/teacher";
                    } else {
                        window.location.href = "http://localhost:8080";
                    }
                }, 1500);
            },
            error: function (error) {
                console.log(error);
                if (error.status === 401) {
                    $('#login-error-msg').html("Sai email hoặc mật khẩu, vui lòng thử lại");
                    return;
                } else if (error.status === 403) {
                    $('#login-error-msg').html("Bạn không được phép truy cập, vui lòng quay lại trang chủ");
                    return;
                } else if (error.status === 404) {
                    $('#login-error-msg').html("Không thể tìm thấy trang web, vui lòng thử lại");
                    return;
                } else if (error.status === 500) {
                    $('#login-error-msg').html("Tài khoản chưa được kích hoạt");
                    return;
                } else {
                    $.toast({
                        heading: 'Lỗi',
                        text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                        icon: 'error',
                        showHideTransition: 'fade',
                        position: 'top-right',
                        loader: false,
                        bgColor: '#FF0000'
                    })
                }
            }
        });

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
        const role = $('#teacherCheckbox').prop('checked') ? 'TEACHER' : 'USER'
        console.log(role);
        RequestBody['role'] = role;
        //call api lên backend
        $.ajax({
            type: "POST",
            url: "/api/v1/authentication/signup",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(RequestBody),
            success: function (data) {
                $.toast({
                    heading: 'Thành công',
                    text: 'Đăng ký tài khoản thành công.',
                    icon: 'success',
                    showHideTransition: 'fade',
                    position: 'top-right',
                    loader: false,
                    bgColor: '#4CAF50'
                })
                setTimeout(() => {
                    window.location.replace("http://localhost:8080/information");
                }, 1500);
            },
            error: function (error) {
                console.log(error);
                //xử lý khi error là 400
                if (error.status === 400) {
                    $('#signup-error-msg').html("Email này đã tồn tại, vui lòng đăng nhập");
                } else {
                    $.toast({
                        heading: 'Lỗi',
                        text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                        icon: 'error',
                        showHideTransition: 'fade',
                        position: 'top-right',
                        loader: false,
                        bgColor: '#FF0000'
                    })
                }
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


});

//gọi api đăng xuất
function logout() {
    $.ajax({
        type: 'POST',
        url: '/api/v1/authentication/logout',
        beforeSend: function (xhr) {
            const token = localStorage.getItem("access-token");
            if (!token || token.trim() === "") {
                return;
            }
            xhr.setRequestHeader('Authorization', "Bearer " + token);
        },
        success: function (data) {
        },
        error: function (error) {
            console.log(error);
        }
    });
    $.toast({
        heading: 'Thành công',
        text: 'Đăng xuất thành công.',
        icon: 'success',
        showHideTransition: 'fade',
        position: 'top-right',
        bgColor: '#4CAF50',
        loader: false,
    })
    localStorage.removeItem('access-token');
    localStorage.removeItem('refresh-token');
    localStorage.removeItem('user-info');
    setTimeout(() => {
        window.location.href = "http://localhost:8080";
    }, 1500);


}

