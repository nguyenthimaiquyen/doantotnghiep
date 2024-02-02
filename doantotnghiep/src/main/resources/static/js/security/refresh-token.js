$(document).ready(function () {

    function parseJwt (token) {
        let base64Url = token.split('.')[1];
        let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        let jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload);
    }

    function refreshAccessToken(refreshToken) {
        $.ajax({
            type: 'POST',
            url: '/api/v1/authentication/refresh-token',
            contentType: "application/json",
            data: JSON.stringify({refreshToken: refreshToken}),
            success: function (data) {
                localStorage.setItem("access-token", data.jwt);
                localStorage.setItem("refresh-token", data.refreshToken);
            },
            error: function (error) {
                console.log(error);
                // window.location.replace("http://localhost:8080/login");
            }
        });
    }

    setInterval(() => {
        let accessToken = localStorage.getItem("access-token");
        let refreshToken = localStorage.getItem("refresh-token");
        if (!accessToken || accessToken.trim() === "") {
            return;
        }
        let jwtJson = parseJwt(accessToken);
        //check thời gian hết hạn, nếu còn dưới 3 phút thì call API
        // if ((jwtJson.exp * 1000 - Date.now()) < (3 * 60 * 1000)) {
        refreshAccessToken(refreshToken);
        // }
    }, 240000); //4 phút chạy 1 lần

});