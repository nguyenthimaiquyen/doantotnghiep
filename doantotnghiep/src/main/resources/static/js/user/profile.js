$(document).ready(() => {

    toastr.options.timeOut = 2500; // 2.5s

    const userInfo = JSON.parse(localStorage.getItem('user-info'));
    const fullName = userInfo ? userInfo.fullName : null;

    if (fullName) {
        $('#profile-fullName').val(fullName);
    }




});