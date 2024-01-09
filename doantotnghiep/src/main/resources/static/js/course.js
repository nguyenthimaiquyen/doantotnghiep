$(document).ready(() => {

    toastr.options.timeOut = 2500; // 2.5s

    //lấy dữ liệu đầu vào
    function getCourseFeeUnit() {
        $.ajax({
            url: "/courses/management/courseFeeUnit",
            type: 'GET',
            contentType: "application/json; charset=utf-8",
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
            url: "/courses/management/difficultyLevel",
            type: 'GET',
            contentType: "application/json; charset=utf-8",
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

    function getTrainingFields() {
        $.ajax({
            url: "/courses/management/trainingField",
            type: 'GET',
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                if ($('#trainingFields').children().length === 0) {
                    if (!data || data.length === 0) {
                        return;
                    }
                    let trainingFieldOptions = "";
                    for (let i = 0; i < data.length; i++) {
                        trainingFieldOptions += "<option value='" + data[i].fieldName + "'>" + data[i].fieldName + "</option>";
                    }
                    $('#trainingFields').append($(trainingFieldOptions));
                }
            },
            error: function (data) {
                console.log("lỗi load dữ liệu")
                toastr.warning(data.responseJSON.error);
            }
        });
    }

    function getDiscountCodes() {
        $.ajax({
            url: "/courses/management/discountCode",
            type: 'GET',
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                if ($('#discountID').children().length === 0) {
                    if (!data || data.length === 0) {
                        return;
                    }
                    let discountCodeOptions = "";
                    for (let i = 0; i < data.length; i++) {
                        discountCodeOptions += "<option value='" + data[i].codeName + "'>" + data[i].codeName + "</option>";
                    }
                    $('#discountID').append($(discountCodeOptions));
                }
            },
            error: function (data) {
                console.log("lỗi load dữ liệu")
                toastr.warning(data.responseJSON.error);
            }
        });
    }

    getTrainingFields();
    getCourseFeeUnit();
    getDifficultyLevel();
    getDiscountCodes();

    setInterval(function () {
        getCourseFeeUnit();
        getDifficultyLevel();
        getTrainingFields();
        getDiscountCodes();
    }, 900000); // 15p chạy 1 lần

    //xử lý nhấn nút thêm mới bài học
    let lessonContent = `
                                <div class="col-xl-11 col-lg-11 col-md-11 col-sm-11 col-11 my-3 border border-dark rounded">
                                    <div class="row">
                                        <div class="col-11">
                                            <div class="checkout-form-list my-3">
                                                <label for="lesson-title">Tiêu đề bài học<span class="text-danger">*</span></label>
                                                <input type="text" placeholder="Nhập tiêu đề" class="w-100" id="lesson-title"
                                                       name="lesson-title">
                                            </div>
                                            <div class="checkout-form-list mb-3">
                                                <label for="description">Nội dung bài học</label>
                                                <textarea id="description" placeholder="Nhập mô tả" name="description"
                                                          class="primary-border2 w-100 pl-15"></textarea>
                                            </div>
                                            <label>Tải video</label>
                                            <div class="custom-file pmd-custom-file-filled mt-1 mb-4">
                                                <input type="file" class="custom-file-input" id="course-video">
                                                <label class="custom-file-label" for="course-video">Không có file nào được chọn</label>
                                            </div>
                                            <div class="checkout-form-list mb-4">
                                                <label for="embedded-video">Nhúng video</label>
                                                <input type="text" placeholder="Nhập link nhúng" class="w-100" id="embedded-video"
                                                       name="embedded-video">
                                            </div>
                                        </div>
                                        <div class="col-1">
                                            <a class="btn delete-btn text-dark delete-test-btn">
                                                <i class="fas fa-times"></i></a>
                                        </div>
                                    </div>
                                </div>`;
    let testContent = `
                                <div class="col-xl-11 col-lg-11 col-md-11 col-sm-11 col-11 mb-3 border border-dark rounded">
                                    <div class="row">
                                        <div class="col-11">
                                            <div class="checkout-form-list my-3">
                                                <label for="question">Câu hỏi</label>
                                                <textarea id="question" placeholder="Nhập câu hỏi" name="question"
                                                          class="primary-border2 w-100 pl-15"></textarea>
                                            </div>
                                            <div class="checkout-form-list mb-4">
                                                <label for="answer">Đáp án</label>
                                                <input type="text" placeholder="Nhập đáp án" class="w-100" id="answer"
                                                       name="answer">
                                            </div>                                       
                                        </div>
                                        <div class="col-1">
                                            <a class="btn delete-btn text-dark delete-test-btn">
                                                <i class="fas fa-times"></i></a>
                                        </div>
                                    </div>
                                </div>`;

    $('.create-lesson-btn').click(() => {
        $('#accordion-tab-1-content-1').append(lessonContent);
    });
    $('.create-test-btn').click(() => {
        $('#accordion-tab-1-content-1').append(testContent);
    });
    $('.delete-lesson-btn').click(() => {
        console.log($('.delete-lesson-btn').parent().parent().parent());
        console.log($('.delete-lesson-btn').parent().parent().parent().remove())
    });
    $('.delete-test-btn').click(() => {
        console.log($('.delete-test-btn').parent().parent().parent());
        console.log($('.delete-test-btn').parent().parent().parent().remove())
    });


});