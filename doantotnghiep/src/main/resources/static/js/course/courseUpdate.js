function initDataUpdate() {
    let courseData = JSON.parse(localStorage.getItem('course-data'));
    //đổ dữ liệu vào form
    $('#course-form #title').val(courseData.title);
    $('#course-form #description').val(courseData.description);
    $('#course-form #learningObjectives').val(courseData.learningObjectives);
    $('#course-form #courseFee').val(courseData.courseFee);
    $('#course-form #courseFeeUnit').val(courseData.courseFeeUnit);
    $('#course-form #difficultyLevel').val(courseData.difficultyLevel.name);
    $('#course-form #trainingFields').val(courseData.trainingFields.fieldName);
    $('#course-form #teacherID').val(courseData.teacherName);
    $('#course-form #discountID').val(courseData.discountCode.codeName);
}

initDataUpdate();


