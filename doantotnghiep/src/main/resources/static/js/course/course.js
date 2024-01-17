$(document).ready(() => {

    toastr.options.timeOut = 2500; // 2.5s

    let deleteCourseId = -1;

    //thêm nội dung nếu role là admin
    let adminData = `
                                <div class="col-xl-6 col-lg-6 col-md-12 col-sm-12 col-12">
                                    <div class="country-select mt-3">
                                        <label for="teacherID">Giảng viên</label>
                                        <select class="w-100" id="teacherID" name="teacherID">

                                        </select>
                                    </div>
                                </div>
                                <div  class="col-xl-6 col-lg-6 col-md-12 col-sm-12 col-12" >
                                    <div class="country-select mt-3">
                                        <label for="discountID">Mã giảm giá</label>
                                        <select class="w-100" id="discountID" name="discountID">

                                        </select>
                                    </div>
                                </div>`;

    const userInfo = JSON.parse(localStorage.getItem('user-info'));
    const userRole = userInfo ? userInfo.roles : null;
    if (userRole == 'ADMIN') {
        $('#course-common-data').append(adminData);
    }

    //lấy dữ liệu đầu vào
    function getCourseFeeUnit() {
        $.ajax({
            url: "/api/v1/courses/courseFeeUnit",
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
            url: "/api/v1/courses/difficultyLevel",
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
            url: "/api/v1/courses/trainingField",
            type: 'GET',
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                if ($('#trainingFieldID').children().length === 0) {
                    if (!data || data.length === 0) {
                        return;
                    }
                    let trainingFieldOptions = "";
                    for (let i = 0; i < data.length; i++) {
                        trainingFieldOptions += "<option value='" + data[i].id + "'>" + data[i].fieldName + "</option>";
                    }
                    $('#trainingFieldID').append($(trainingFieldOptions));
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
            url: "/api/v1/courses/discountCode",
            type: 'GET',
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                if ($('#discountID').children().length === 0) {
                    if (!data || data.length === 0) {
                        return;
                    }
                    let discountCodeOptions = "<option value=''>--</option>";
                    for (let i = 0; i < data.length; i++) {
                        discountCodeOptions += "<option value='" + data[i].id + "'>" + data[i].codeName + "</option>";
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

    function getTeachers() {
        $.ajax({
            url: "/api/v1/courses/teachers",
            type: 'GET',
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                if ($('#teacherID').children().length === 0) {
                    if (!data || data.length === 0) {
                        return;
                    }
                    let teachersOptions = "<option value=''>--</option>";
                    for (let i = 0; i < data.length; i++) {
                        teachersOptions += "<option value='" + data[i].id + "'>" + data[i].user.fullName + "</option>";
                    }
                    $('#teacherID').append($(teachersOptions));
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
    getTeachers();

    setInterval(function () {
        getCourseFeeUnit();
        getDifficultyLevel();
        getTrainingFields();
        getDiscountCodes();
        getTeachers();
    }, 900000); // 15p chạy 1 lần

//1. chức năng thêm sửa xóa thông tin chung của khóa học

    //validate form thông tin chung của khóa học
    const validator = $('#course-form').validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            'title': {
                required: true,
                maxlength: 255
            },
            "courseFee": {
                required: true
            },
            "courseFeeUnit": {
                required: true
            },
            "difficultyLevel": {
                required: true
            },
            "trainingFields": {
                required: true
            }
        },
        messages: {
            'title': {
                required: "Tiêu đề bắt buộc",
                maxlength: "Tiêu đề dài tối đa 255 ký tự"
            },
            'courseFee': {
                required: "Giá khóa học bắt buộc"
            },
            "courseFeeUnit": {
                required: "Đơn vị giá bắt buộc"
            },
            "difficultyLevel": {
                required: "Cấp độ bắt buộc"
            },
            "trainingFields": {
                required: "Lĩnh vực đào tạo bắt buộc"
            }
        }
    });

    //xử lý ấn menu của course
    $('.create-course-btn').click(function () {
        localStorage.setItem('action-type', 'CREATE');
        window.location.replace("http://localhost:8080/courses/creation");
    });

    $('.add-section-btn').click(function (event) {
        const courseId = $(event.currentTarget).attr("course-id");
        localStorage.setItem('course-id', courseId);
        window.location.replace("http://localhost:8080/courses/"+ courseId + "/sections");
    });


    //lấy dữ liệu từ backend và đổ lên form để update khóa học
    $('.update-course-btn').click(function (event) {
        //call api lên backend để lấy dữ liệu
        const updateCourseId = parseInt($(event.currentTarget).attr("course-id"));
        let course = null;
        $.ajax({
            url: "/api/v1/courses/" + updateCourseId,
            type: "GET",
            success: function (data) {
                course = data;
                console.log(data)
            },
            error: function (err) {
                console.log(err.responseText);
                toastr.warning("Đã có lỗi xảy ra, vui lòng thử lại!");
            }
        });

        if (!course) {
            toastr.error("Đã có lỗi xảy ra, vui lòng thử lại!")
            return;
        }
        localStorage.setItem('course-data', course);
        localStorage.setItem('action-type', 'UPDATE');
        localStorage.setItem('course-id', JSON.stringify(updateCourseId));
        window.location.replace("http://localhost:8080/courses/creation");
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

    });

    //create or update a course
    $('#save-course-btn').click(function (event) {
        //validate
        const isValidForm = $('#course-form').valid();
        if (!isValidForm) {
            return;
        }
        //lấy dữ liệu từ form
        const formCourseData = $('#course-form').serializeArray();
        if (!formCourseData || formCourseData.length === 0) {
            return;
        }
        //chuyển dữ liệu từ object sang json
        const courseRequestBody = {};
        for (let i = 0; i < formCourseData.length; i++) {
            courseRequestBody[formCourseData[i].name] = formCourseData[i].value;
        }
        const actionType = localStorage.getItem('action-type');
        console.log(actionType)
        const courseId = localStorage.getItem('course-id');
        console.log(courseId)
        const method = actionType == "CREATE" ? "POST" : "PUT";
        if (method == "PUT") {
            courseRequestBody["id"] = courseId;
        }
        console.log(method)
        console.log(courseRequestBody)
        //call api lên backend
        $.ajax({
            url: "/api/v1/courses",
            type: method,
            data: JSON.stringify(courseRequestBody),
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                toastr.success((method === "CREATE" ? "Tạo mới " : "Cập nhật ") + "thành công khóa học!");
                setTimeout(() => {
                    window.location.replace("http://localhost:8080/courses/management");
                }, 1000);
            },
            error: function (error) {
                console.log(error.responseText);
                toastr.warning("Đã có lỗi xảy ra, vui lòng thử lại!");
            }
        });
        localStorage.removeItem("action-type");
        localStorage.removeItem("course-id");
    });

    //quay lại trang quản lý khóa học
    $('#get-back-btn').click(() => {
        localStorage.removeItem("action-type");
        localStorage.removeItem("course-id");
        window.location.replace("http://localhost:8080/courses/management");

    });

    //show modal to delete a course
    $('.delete-course-btn').click(function (event) {
        deleteCourseId = parseInt($(event.currentTarget).attr("course-id"));
        $('#course-delete-modal').modal('show');
    });

    //delete a course
    $('#delete-course-btn').click(function () {
        $.ajax({
            url: "/api/v1/courses/" + deleteCourseId,
            type: "DELETE",
            success: function (data) {
                toastr.success("Xóa khóa học thành công!");
                setTimeout(() => {
                    location.reload();
                }, 1000);
            },
            error: function (err) {
                toastr.warning("Đã có lỗi xảy ra, vui lòng thử lại!");
            }
        });
    });



});


let sectionCount = 2;
let lessonCount = 1
let testCount = 1;


function addSection() {
    let newSection = `
                        <div class="card border-0 mb-2">
                            <div class="card-header border-bottom-0 p-0" id="accordion-tab-1-heading-${sectionCount}">
                                <div class="row">
                                    <div class="col-xl-11  col-lg-11  col-md-11  col-sm-11 col-11">
                                        <h6 class="mb-0">
                                            <a href="#"
                                               class="btn btn-link main-color f-500 d-block text-left rounded-0 position-relative d-flex align-items-center justify-content-between"
                                               data-toggle="collapse" data-target="#accordion-tab-1-content-${sectionCount}"
                                               aria-expanded="false" aria-controls="accordion-tab-1-content-${sectionCount}">Nội dung chương
                                            </a>
                                        </h6>
                                    </div>
                                    <div class="col-xl-1  col-lg-1  col-md-1  col-sm-1 col-1">
                                        <div class="col-1">
                                            <a class="btn delete-btn text-dark" onclick="deleteSection('accordion-tab-1-heading-${sectionCount}')">
                                                <i class="fas fa-times"></i></a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="collapse show" id="accordion-tab-1-content-${sectionCount}"
                                 aria-labelledby="accordion-tab-1-heading-${sectionCount}" data-parent="#accordion-tab-1">
                                <div class="col-xl-11  col-lg-11  col-md-11  col-sm-11 col-11">
                                    <div class="checkout-form-list mt-4 mb-3">
                                        <label for="section-title-${sectionCount}">Tiêu đề chương<span
                                                class="text-danger">*</span></label>
                                        <input type="text" placeholder="Nhập tiêu đề" class="w-100" id="section-title-${sectionCount}"
                                               name="section-title">
                                    </div>
                                </div>
                                <div class="col-lg-12 my-4">
                                    <div class="row">
                                        <div class="col-xl-3  col-lg-3  col-md-6  col-sm-6 col-12">
                                            <div class="d-flex justify-content-start mt-3 mt-lg-0 ml-5">
                                                <a class="btn btn-primary f-500" onclick="addLessonContent('accordion-tab-1-content-${sectionCount}')">
                                                    <i class="fas fa-plus mr-2"></i>Thêm bài học</a>
                                            </div><!-- /my-btn -->
                                        </div>
                                        <div class="col-xl-3  col-lg-3  col-md-6  col-sm-6 col-12">
                                            <div class="d-flex justify-content-end mt-3 mt-lg-0">
                                                <a class="btn btn-primary f-500" onclick="addTestContent('accordion-tab-1-content-${sectionCount}')">
                                                    <i class="fas fa-plus mr-2"></i>Thêm bài kiểm tra</a>
                                            </div><!-- /my-btn -->
                                        </div>
                                    </div>
                                </div><!-- /col -->
                            </div>
                        </div>`;
    $('#accordion-tab-1').append(newSection);
    sectionCount++;
}

function addLessonContent(sectionId) {
    let lessonContent = `
                                <div class="col-xl-11 col-lg-11 col-md-11 col-sm-11 col-11 my-3 border border-dark rounded" 
                                    id="lesson-${lessonCount}">
                                    <div class="row">
                                        <div class="col-11">
                                            <form id="lesson-form-${lessonCount}">
                                                <div class="checkout-form-list my-3">
                                                <label for="lesson-title-${lessonCount}">Tiêu đề bài học<span class="text-danger">*</span></label>
                                                <input type="text" placeholder="Nhập tiêu đề" class="w-100" id="lesson-title-${lessonCount}"
                                                       name="lesson-title-${lessonCount}">
                                                </div>
                                                <div class="checkout-form-list mb-3">
                                                    <label for="description-${lessonCount}">Nội dung bài học</label>
                                                    <textarea id="description-${lessonCount}" placeholder="Nhập mô tả" name="description-${lessonCount}"
                                                              class="primary-border2 w-100 pl-15"></textarea>
                                                </div>
                                                <label>Tải video</label>
                                                <div class="custom-file pmd-custom-file-filled mt-1 mb-4">
                                                    <input type="file" class="custom-file-input" id="course-video-${lessonCount}">
                                                    <label class="custom-file-label" for="course-video">Không có file nào được chọn</label>
                                                </div>
                                                <div class="checkout-form-list mb-4">
                                                    <label for="embedded-video-${lessonCount}">Nhúng video</label>
                                                    <input type="text" placeholder="Nhập link nhúng" class="w-100" id="embedded-video-${lessonCount}"
                                                           name="embedded-video-${lessonCount}">
                                                </div>
                                            </form>
                                        </div>
                                        <div class="col-1">
                                            <a class="btn delete-btn text-dark" onclick="deleteLesson('lesson-${lessonCount}')">
                                                <i class="fas fa-times"></i></a>
                                        </div>
                                    </div>
                                </div>`;
    $('#' + sectionId).append(lessonContent);
    lessonCount++;
}

function addTestContent(sectionId) {
    let testContent = `
                                <div class="col-xl-11 col-lg-11 col-md-11 col-sm-11 col-11 mb-3 border border-dark rounded"
                                    id="test-${testCount}">
                                    <div class="row">
                                        <div class="col-11">
                                            <form id="test-form-${testCount}">
                                                <div class="checkout-form-list my-3">
                                                    <label for="question-${testCount}">Câu hỏi</label>
                                                    <textarea id="question-${testCount}" placeholder="Nhập câu hỏi" name="question-${testCount}"
                                                              class="primary-border2 w-100 pl-15"></textarea>
                                                </div>
                                                <div class="checkout-form-list mb-4">
                                                    <label for="answer-${testCount}">Đáp án</label>
                                                    <input type="text" placeholder="Nhập đáp án" class="w-100" id="answer-${testCount}"
                                                           name="answer-${testCount}">
                                                </div> 
                                            </form>                                      
                                        </div>
                                        <div class="col-1">
                                            <a class="btn delete-btn text-dark" onclick="deleteTest('test-${testCount}')">
                                                <i class="fas fa-times"></i></a>
                                        </div>
                                    </div>
                                </div>`;
    $('#' + sectionId).append(testContent);
    testCount++;
}

function deleteSection(sectionId) {
    $('#' + sectionId).closest('.card').remove();
}

function deleteLesson(lessonId) {
    $('#' + lessonId).remove();
}

function deleteTest(testID) {
    $('#' + testID).remove();
}