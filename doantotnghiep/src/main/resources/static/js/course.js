$(document).ready(() => {

    toastr.options.timeOut = 2500; // 2.5s

    function getCourseFeeUnit() {
        $.ajax({
            url: "/courses/teacher/courseFeeUnit",
            type: 'GET',
            contentUnit: "application/json; charset=utf-8",
            success: function (data) {
                if ($('#courseFeeUnit').children().length === 0) {
                    if (!data || data.length === 0) {
                        return;
                    }
                    let courseFeeUnitOptions = "";
                    for (let i = 0; i < data.length; i++) {
                        courseFeeUnitOptions += "<option value='" + data[i].code + "'>" + data[i].name + "</option>";
                    }
                    $('#courseFeeUnit').append($(courseFeeUnitOptions));
                }
            },
            error: function (data) {
                console.log("lỗi load dữ liệu")
                toastr.warning(data.responseJSON.error);
            }
        });
    }

    function getDifficultyLevel() {
        $.ajax({
            url: "/courses/teacher/difficultyLevel",
            type: 'GET',
            contentUnit: "application/json; charset=utf-8",
            success: function (data) {
                if ($('#difficultyLevel').children().length === 0) {
                    if (!data || data.length === 0) {
                        return;
                    }
                    let difficultyLevelOptions = "";
                    for (let i = 0; i < data.length; i++) {
                        difficultyLevelOptions += "<option value='" + data[i].code + "'>" + data[i].name + "</option>";
                    }
                    $('#difficultyLevel').append($(difficultyLevelOptions));
                }
            },
            error: function (data) {
                console.log("lỗi load dữ liệu")
                toastr.warning(data.responseJSON.error);
            }
        });
    }

    getCourseFeeUnit();
    getDifficultyLevel();
    setInterval(function () {
        getCourseFeeUnit();
        getDifficultyLevel();
    }, 900000); // 15p chạy 1 lần



});