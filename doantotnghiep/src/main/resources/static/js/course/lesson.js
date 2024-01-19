


let deleteLessonId = -1;

//2. chức năng thêm sửa xóa lesson

//ấn thêm chương thì hiện modal
$('.create-lesson-btn').click(() => {
    $('#lesson-creation-modal #save-lesson-btn').attr("action-type", "CREATE");
    $('#lesson-creation-modal').modal('show');
});

//open modal to update a lesson
$('.update-lesson-btn').click(async function (event) {
    //call api lên java để lấy dữ liệu
    const updateLessonId = parseInt($(event.currentTarget).attr("lesson-id"));
    let lesson = null;
    await $.ajax({
        url: "/api/v1/discount-codes/" + updateLessonId,
        type: "GET",
        success: function (data) {
            lesson = data;
        },
        error: function (err) {
            $.toast({
                heading: 'Lỗi',
                text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                icon: 'error',
                showHideTransition: 'fade',
                position: 'top-right',
                loader: false,
                bgColor: '#FF0000'
            });
            console.log(err);
        }
    });

    if (!lesson) {
        $.toast({
            heading: 'Lỗi',
            text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
            icon: 'error',
            showHideTransition: 'fade',
            position: 'top-right',
            loader: false,
            bgColor: '#FF0000'
        });
        return;
    }

    //đổ dữ liệu vào form
    $('#lesson-form #codeName').val(lesson.codeName);
    $('#lesson-form #discountValue').val(lesson.discountValue);
    $('#lesson-form #discountUnit').val(lesson.discountUnit);
    $('#lesson-form #startDate').val(lesson.startDate);
    $('#lesson-form #endDate').val(lesson.endDate);
    $('#lesson-form #usageLimitationCount').val(lesson.usageLimitationCount);

    $('#lesson-modal #save-lesson-btn').attr('action-type', "UPDATE");
    $('#lesson-modal #save-lesson-btn').attr("lesson-id", updateLessonId);
    $('#lesson-modal').modal("show");
});


//show modal to delete a lesson
$('.delete-lesson-btn').click(function (event) {
    deleteLessonId = parseInt($(event.currentTarget).attr("lesson-id"));
    $('#lesson-delete-modal').modal('show');
});

//delete a lesson
$('#delete-lesson-btn').click(function () {
    $.ajax({
        url: "/api/v1/discount-codes/" + deleteLessonId,
        type: "DELETE",
        success: function (data) {
            $.toast({
                heading: 'Thành công',
                text: "Xóa mã giảm giá thành công!",
                icon: 'success',
                showHideTransition: 'fade',
                position: 'top-right',
                loader: false,
                bgColor: '#4CAF50'
            });
            setTimeout(() => {
                location.reload();
            }, 1000);
        },
        error: function (err) {
            $.toast({
                heading: 'Lỗi',
                text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                icon: 'error',
                showHideTransition: 'fade',
                position: 'top-right',
                loader: false,
                bgColor: '#FF0000'
            });
        }
    });
});

//reset form
$('#lesson-modal').on('hidden.bs.modal', function () {
    $("#lesson-modal #save-lesson-btn").attr("action-type", "");
    $('#lesson-modal #save-lesson-btn').attr("lesson-id", "");
    $('#lesson-form').trigger("reset");
    $('#lesson-form input').removeClass("error");
    validator.resetForm();
});




