$(document).ready(() => {

    let deleteLessonId = -1;

//show modal to delete a lesson
    $('.delete-lesson-btn').click(function (event) {
        deleteLessonId = parseInt($(event.currentTarget).attr("lesson-id"));
        $('#lesson-delete-modal').modal('show');
    });

//delete a lesson
    $('#delete-lesson-btn').click(function () {
        $.ajax({
            url: "/api/v1/lessons/" + deleteLessonId,
            type: "DELETE",
            success: function (data) {
                $.toast({
                    heading: 'Thành công',
                    text: "Xóa bài học thành công!",
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


});

