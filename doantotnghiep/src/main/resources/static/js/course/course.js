$(document).ready(() => {


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
                    let teachersOptions = "<option value=''>--</option>";
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
        $('#course-modal #save-course-btn').attr("action-type", "CREATE");
        $('#course-modal').modal('show');
    });

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

        if (!course) {
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

        $('#course-form #title').val(course.title);
        $('#course-form #description').val(course.description);
        $('#course-form #learningObjectives').val(course.learningObjectives);
        $('#course-form #courseFee').val(course.courseFee);
        $('#course-form #courseFeeUnit').val(course.courseFeeUnit);
        $('#course-form #difficultyLevel').val(course.difficultyLevel);
        $('#course-form #trainingFieldID').val(course.trainingField.id);
        $('#course-form #teacherID').val(course.teacher.id);
        $('#course-form #discountID').val(course.discountCode.id);

        $('#course-modal #save-course-btn').attr('action-type', "UPDATE");
        $('#course-modal #save-course-btn').attr("course-id", updateCourseId);
        $('#course-modal').modal("show");

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
                    heading: 'Thành công',
                    text: (method === "CREATE" ? "Tạo mới " : "Cập nhật ") + "thành công khóa học!",
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
            error: function (error) {
                console.log(error.responseText);
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
                    heading: 'Thành công',
                    text: "Xóa khóa học thành công!",
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

