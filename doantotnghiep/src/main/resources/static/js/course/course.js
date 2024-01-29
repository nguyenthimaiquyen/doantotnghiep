$(document).ready(() => {

    let deleteCourseId = -1;
    let courseId = -1;
    let courseStatus = "";

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
    if (userRole == 'TEACHER') {
        $('#WAITING_FOR_REVIEW').remove();
        $('#APPROVED').remove();
        $('#REJECTED').remove();
        $('#UNPUBLISHED').remove();
        $('#PUBLISHED').remove();
        $('#ARCHIVED').remove();
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
                    let teachersOptions = "";
                    for (let i = 0; i < data.length; i++) {
                        teachersOptions += "<option value='" + data[i].id + "'>" + data[i].user.fullName + "</option>";
                    }
                    $('#teacherID').append($(teachersOptions));
                }
            },
            error: function (data) {
                console.log("lỗi load dữ liệu")
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
            "description": {
                maxlength: 1000
            },
            "learningObjectives": {
                maxlength: 1000
            },
            "courseFee": {
                required: true,
                min: 0
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
            "description": {
                maxlength: "Mô tả khóa học dài tối đa 1000 ký tự"
            },
            "learningObjectives": {
                maxlength: "Mục tiêu học tập dài tối đa 1000 ký tự"
            },
            'courseFee': {
                required: "Giá khóa học bắt buộc",
                min: "Giá khóa học không là số âm"
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

    //mở modal tạo mới khóa học
    $('.create-course-btn').click(function () {
        $('#course-modal #save-course-btn').attr("action-type", "CREATE");
        $('#course-modal #save-course-btn').prop("disabled", true);
        $('#course-modal').modal('show');
    });

    //disable nút lưu khi chưa nhập các trường thông tin bắt buộc của khóa học
    $('#course-form #title, #course-form #courseFee').on("input", function () {
        let allFieldsFilled = checkAllFieldsFilled();
        $('#course-modal #save-course-btn').prop("disabled", !allFieldsFilled);
    });

    //hàm check các các trường bắt buộc đã nhập thông tin chưa
    function checkAllFieldsFilled() {
        let allFields = $('#course-form #title, #course-form #courseFee');
        let allFieldsFilled = true;
        allFields.each(function () {
            if ($(this).val === "") {
                allFieldsFilled = false;
                return false;
            }
        });
        return allFieldsFilled;
    }

    //chọn chức năng thêm chương trình học thì lưu id của course vào local storage
    $('.add-section-btn').click(function (event) {
        const courseId = $(event.currentTarget).attr("course-id");
        localStorage.setItem("course-id", courseId);
        window.location.replace("http://localhost:8080/courses/"+ courseId + "/sections");
    });

    //lấy dữ liệu từ backend và đổ lên form để update khóa học
    $('.update-course-btn').click(async function (event) {
        //call api lên backend để lấy dữ liệu
        const updateCourseId = parseInt($(event.currentTarget).attr("course-id"));
        let course = null;
        await $.ajax({
            url: "/api/v1/courses/" + updateCourseId,
            type: "GET",
            success: function (data) {
                course = data;
            },
            error: function (err) {
                console.log(err.responseText);
                $.toast({
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    position: 'top-right',
                    bgColor: '#FF0000'
                });
            }
        });

        if (!course) {
            $.toast({
                text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                icon: 'error',
                position: 'top-right',
                bgColor: '#FF0000'
            });
            return;
        }

        $('#course-form #title').val(course.title);
        $('#course-form #description').val(course.description);
        $('#course-form #learningObjectives').val(course.learningObjectives);
        $('#course-form #courseFee').val(course.courseFee);
        $('#course-form #courseFeeUnit').val(course.courseFeeUnit);
        $('#course-form #difficultyLevel').val(course.difficultyLevel);
        $('#course-form #trainingFieldID').val(course.trainingField.id);
        $('#course-form #teacherID').val(course.teacher ? course.teacher.id : '');
        $('#course-form #discountID').val(course.discountCode ? course.discountCode.id : '');

        $('#course-modal #save-course-btn').attr('action-type', "UPDATE");
        $('#course-modal #save-course-btn').attr("course-id", updateCourseId);
        $('#course-modal').modal("show");

    });

    //create or update a course
    $('#save-course-btn').click(function (event) {
        //disable nút lưu khi người dùng ấn lưu
        $('#course-modal #save-course-btn').prop("disabled", true);
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
        const actionType = parseInt($(event.currentTarget).attr("action-type"));
        const courseId = parseInt($(event.currentTarget).attr("course-id"));
        const method = actionType == "CREATE" ? "POST" : "PUT";
        if (method == "PUT") {
            courseRequestBody["id"] = courseId;
        }
        //call api lên backend
        $.ajax({
            url: "/api/v1/courses",
            type: method,
            data: JSON.stringify(courseRequestBody),
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                $.toast({
                    text: (method === "POST" ? "Tạo mới " : "Cập nhật ") + "thành công khóa học!",
                    icon: 'success',
                    position: 'top-right',
                    bgColor: '#4CAF50'
                });
                setTimeout(() => {
                    location.reload();
                    $('#course-modal #save-course-btn').prop("disabled", false);
                }, 3000);
            },
            error: function (error) {
                console.log(error.responseText);
                $.toast({
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    position: 'top-right',
                    bgColor: '#FF0000'
                });
            }
        });
        $("#course-modal #save-course-btn").attr("action-type", "");
        $('#course-modal #save-course-btn').attr("course-id", "");
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
                $.toast({
                    text: "Xóa khóa học thành công!",
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
    });

    //reset form
    $('#course-modal').on('hidden.bs.modal', function () {
        $("#course-modal #save-course-btn").attr("action-type", "");
        $('#course-modal #save-course-btn').attr("course-id", "");
        $('#course-form').trigger("reset");
        $('#course-form input').removeClass("error");
        validator.resetForm();
    });

    //mở modal khi ấn nút yêu cầu thay đổi trạng thái khóa học
    $('.course-status-btn').click((event) => {
        courseId = parseInt($(event.currentTarget).attr("course-id"));
        courseStatus = $(event.currentTarget).attr("course-status");
        switch (courseStatus) {
            case 'DRAFT':
                $('#course-review-modal').modal('show');
                break;
            case 'WAITING_FOR_REVIEW':
                $('#course-approve-modal').modal('show');
                break;
            case 'APPROVED':
                $('#course-publish-modal').modal('show');
                break;
            case 'PUBLISHED':
                $('#course-unpublish-modal').modal('show');
                break;
            case 'REJECTED':
            case 'UNPUBLISHED':
                $('#course-archive-modal').modal('show');
                break;
            default:
                $('#course-review-modal').modal('show');
        }
    });

    $('#review-course-btn').click(() => {
        const courseStatusRequestBody = {};
        courseStatusRequestBody["courseStatus"] = 'WAITING_FOR_REVIEW';
        changeCourseStatus(courseId, courseStatusRequestBody)
    });

    $('#approve-course-btn').click(() => {
        const courseStatusRequestBody = {};
        courseStatusRequestBody["courseStatus"] = 'APPROVED';
        changeCourseStatus(courseId, courseStatusRequestBody)
    });

    $('#reject-course-btn').click(() => {
        const courseStatusRequestBody = {};
        courseStatusRequestBody["courseStatus"] = 'REJECTED';
        changeCourseStatus(courseId, courseStatusRequestBody)
    });

    $('#publish-course-btn').click(() => {
        const courseStatusRequestBody = {};
        courseStatusRequestBody["courseStatus"] = 'PUBLISHED';
        changeCourseStatus(courseId, courseStatusRequestBody)
    });

    $('#unpublish-course-btn').click(() => {
        const courseStatusRequestBody = {};
        courseStatusRequestBody["courseStatus"] = 'UNPUBLISHED';
        changeCourseStatus(courseId, courseStatusRequestBody)
    });

    $('#archive-course-btn').click(() => {
        const courseStatusRequestBody = {};
        courseStatusRequestBody["courseStatus"] = 'ARCHIVED';
        changeCourseStatus(courseId, courseStatusRequestBody)
    });

    //lắng nghe sự kiện người dùng nhấn enter khi search course
    $("#search-course").keypress(function (event) {
        if (event.which == 13) {
            $('#search-course-form').submit();
            return false;
        }
    });

    //set url mới khi thẻ select thay đổi
    $('#course-page-size').change(function (event) {
        const pageSize = event.target.value;
        window.location.href = ('/courses/management?pageSize=' + pageSize + '&currentPage=0');
    });


});

function getCourseMessage(courseStatus) {
    switch (courseStatus) {
        case 'WAITING_FOR_REVIEW':
            return 'Khóa học đã được gửi cho admin xem xét';
        case 'APPROVED':
            return 'Khóa học đã được phê duyệt';
        case 'PUBLISHED':
            return 'Khóa học đã được công bố thành công';
        case 'UNPUBLISHED':
            return 'Khóa học đã được dừng công bố';
        case 'ARCHIVED':
            return 'Khóa học đã được đưa vào lưu trữ';
        case 'REJECTED':
            return 'Khóa học đã bị từ chối';
        default:
            return 'Khóa học đã được xem xét lại';
    }
}

function changeCourseStatus(courseId, courseStatusRequestBody) {
    $.ajax({
        url: "/api/v1/courses/" + courseId,
        type: "PUT",
        data: JSON.stringify(courseStatusRequestBody),
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            const courseMessage = getCourseMessage(courseStatusRequestBody.courseStatus);
            $.toast({
                text: courseMessage,
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

