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