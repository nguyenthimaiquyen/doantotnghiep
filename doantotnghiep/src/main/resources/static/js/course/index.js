$(document).ready(function () {

    $('.show-course-btn').click(function (event) {
        let courseId = parseInt($(event.currentTarget).attr("course-id"));
        window.location.replace("http://localhost:8080/courses/" + courseId);
    });

    //lắng nghe sự kiện người dùng nhấn enter khi search course
    $("#search-course").keypress(function (event) {
        if (event.which == 13) {
            $('#search-course-form').submit();
            return false;
        }
    });


});