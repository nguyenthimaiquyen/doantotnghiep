$(document).ready(() => {

    let accountId = -1;
    let accountStatus = '';

    //lắng nghe sự kiện người dùng nhấn enter khi search
    $("#search-account").keypress(function (event) {
        if (event.which == 13) {
            $('#search-account-form').submit();
            return false;
        }
    });

    //set url mới khi thẻ select thay đổi
    $('#account-page-size').change(function (event) {
        const pageSize = event.target.value;
        window.location.href = ('/accounts?pageSize=' + pageSize + '&currentPage=0');
    });

    //mở modal cấm tài khoản hoặc dừng cấm tài khoản
    $('.account-status-btn').click(function (event) {
        accountId = parseInt($(event.currentTarget).attr("account-id"));
        accountStatus = $(event.currentTarget).attr("account-status");
        switch (accountStatus) {
            case 'ACTIVATED':
                $('#account-ban-modal').modal('show');
                break;
            case 'BANNED':
                $('#account-activate-modal').modal('show');
                break;
        }
    });

    //xử lý sự kiện click thay đổi trạng thái account
    $('#activate-account-btn').click(() => {
        const accountStatusRequestBody = {};
        accountStatusRequestBody["userStatus"] = 'ACTIVATED';
        changeAccountStatus(accountId, accountStatusRequestBody)
    });

    $('#ban-account-btn').click(() => {
        const accountStatusRequestBody = {};
        accountStatusRequestBody["userStatus"] = 'BANNED';
        changeAccountStatus(accountId, accountStatusRequestBody)
    });


});

function changeAccountStatus(accountId, accountStatusRequestBody) {
    console.log(accountStatusRequestBody.userStatus)
    $.ajax({
        url: "/api/v1/accounts/" + accountId,
        type: "PUT",
        data: JSON.stringify(accountStatusRequestBody),
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            $.toast({
                text: accountStatusRequestBody.userStatus == "BANNED" ? "Tài khoản đã bị cấm" : "Tài khoản đã được kích hoạt thành công",
                icon: 'success',
                position: 'top-right',
                bgColor: '#4CAF50'
            });
            setTimeout(() => {
                location.reload();
            }, 3000);
        },
        error: function (err) {
            $.toast({
                text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                icon: 'error',
                position: 'top-right',
                bgColor: '#FF0000'
            });
        }
    });
}