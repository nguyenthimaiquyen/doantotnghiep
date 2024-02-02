let deleteCourseId = -1;
let courseId = -1;
let courseStatus = "";

//thực hiện chức năng searchCourses bằng jquery
async function searchCourses(pageSize, pageIndex, courseName) {
    const userInfo = JSON.parse(localStorage.getItem('user-info'));
    const userRole = userInfo ? userInfo.roles : null;
    let courseData = null;
    await $.ajax({
        url: "/api/v1/courses",
        type: 'GET',
        contentType: "application/json; charset=utf-8",
        data: {
            pageSize: pageSize,
            pageIndex: pageIndex,
            courseName: courseName
        },
        success: function (data) {
            courseData = data;
        },
        error: function (error) {
            console.log("lỗi load dữ liệu course");
        }
    });
    //đổ dữ liệu ra giao diện
    let courseTable = `
                    <div class="row my-1">
                        <div class="col-lg-12">
                            <div class="d-flex text-center justify-content-center mt-3 mt-lg-0">
                                <h6>Danh sách các khóa học</h6>
                            </div><!-- /my-btn -->
                        </div><!-- /col -->
                    </div>
                    <div class="row my-2">
                        <div class="col-xl-4  col-lg-4  col-md-4  col-sm-10 col-12">
                            <div class="course-search position-relative">
                                <form id="search-course-form" onsubmit="return handleSearchCourseFormSubmit()">
                                    <input type="text" id="search-course" class="form-control pr-50" name="courseName"
                                            placeholder="Nhập tên khóa học">
                                    <span class="position-absolute d-inline-block secondary-color">
                                        <i class="fa fa-search"></i></span>
                                </form>
                            </div>
                        </div>
                        <div class="col-xl-8  col-lg-8  col-md-8  col-sm-10 col-12">
                            <div class="d-flex justify-content-end mt-3 mt-lg-0">
                                <button class="btn btn-primary f-500" onclick="createCourseBtn()">
                                    <i class="fas fa-plus mr-2"></i>Tạo khóa học
                                </button>
                            </div><!-- /my-btn -->
                        </div><!-- /col -->
                    </div>
                    <div class="row my-4">
                        <div class="col-xl-12  col-lg-12  col-md-12  col-sm-12 col-12">
                            <table class="table table-bordered table-hover">
                                <thead>
                                <tr class="text-center">
                                    <th>STT</th>
                                    <th>Tên khóa học</th>
                                    <th>Giá</th>
                                    <th>Đơn vị giá</th>
                                    <th>Cấp độ</th>
                                    <th>Giảng viên</th>
                                    <th>Mã giảm giá</th>
                                    <th>Lĩnh vực</th>
                                    <th>Trạng thái</th>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody id="course-list">
                                <!-- /course-row -->                               
                                </tbody>
                            </table>
                        </div>
                    </div><!-- /row -->
                    <div class="row my-3">
                        <div class="col-xl-2  col-lg-2  col-md-12  col-sm-12 col-12">
                            Tổng số [totalElement] bản ghi</div>
                        <div class="col-xl-10 col-lg-10 col-md-12 col-sm-12 col-12 d-flex justify-content-end align-items-center">
                            <div class="my-3">
                                <span>Hiển thị </span>
                                <select class="mr-1" id="course-page-size">
                                    <option value="6">6</option>
                                    <option value="12">12</option>
                                </select>
                                <span>bản ghi</span>
                            </div>
                            <div class="ml-2">
                                <ul class="pagination justify-content-end mb-0" id="pagination-container">                            
                               
                                </ul>
                            </div>
                        </div>
                    </div>`;

    if (courseName) {
        $('#search-course').val(courseName);
    }

    courseTable = courseTable.replace("[totalElement]", courseData.totalElement);
    $('#course-management').empty();
    $('#course-management').append(courseTable);

    //render dữ liệu course
    $('#course-list').empty();
    $.each(courseData.courses, function (index, course) {
        let courseHTML = `
                                <tr>                    
                                    <td class="text-center">[index]</td>
                                    <td><a class="focused cursor-pointer text-dark" 
                                        onclick="addSection('${course.id}')">${course.title}</a></td>
                                    <td class="text-center">${course.courseFee}</td>
                                    <td class="text-center">${course.courseFeeUnit}</td>
                                    <td class="text-center">[difficultyLevel]</td>
                                    <td class="text-center">${course.teacherName}</td>
                                    <td class="text-center">[discountCode]</td>
                                    <td>
                                        <span id="training-field-badge-${course.id}"></span>
                                    </td>
                                    <td><span>[courseStatus]</span></td>
                                    <td>
                                        <div class="dropdown dropleft">
                                            <button class="btn btn-primary" type="button" data-toggle="dropdown">
                                                <i class="fas fa-ellipsis-v"></i>
                                            </button>
                                            <div class="dropdown-menu" id="course-menu-${course.id}">
                                                <button class="dropdown-item focused cursor-pointer"
                                                        onclick="updateCourse('${course.id}')">Cập nhật khóa học
                                                </button>
                                                <button class="dropdown-item focused cursor-pointer" id="course-status-menu-${course.id}"
                                                        onclick="handleCourseStatus('${course.id}', '${course.courseStatus}')">                                                  
                                                </button>
                                                <!-- /delete-course-btn -->
                                            </div>
                                        </div>
                                    </td>
                                </tr>
            `;
        courseHTML = courseHTML.replace("[index]", (pageIndex * pageSize) + index + 1);
        if (course.discountCodeName) {
            courseHTML = courseHTML.replace("[discountCode]", course.discountCodeName);
        } else {
            courseHTML = courseHTML.replace("[discountCode]", "--");
        }
        switch (course.difficultyLevel) {
            case 'BEGINNER':
                courseHTML = courseHTML.replace("[difficultyLevel]", "Mới bắt đầu");
                break;
            case 'INTERMEDIATE':
                courseHTML = courseHTML.replace("[difficultyLevel]", "Trung cấp");
                break;
            default:
                courseHTML = courseHTML.replace("[difficultyLevel]", "Nâng cao");
        }

        switch (course.courseStatus) {
            case 'DRAFT':
                courseHTML = courseHTML.replace("[courseStatus]", "Phác thảo");
                break;
            case 'WAITING_FOR_REVIEW':
                courseHTML = courseHTML.replace("[courseStatus]", "Chờ xem xét");
                break;
            case 'APPROVED':
                courseHTML = courseHTML.replace("[courseStatus]", "Chấp thuận");
                break;
            case 'REJECTED':
                courseHTML = courseHTML.replace("[courseStatus]", "Bị từ chối");
                break;
            case 'PUBLISHED':
                courseHTML = courseHTML.replace("[courseStatus]", "Đã công bố");
                break;
            case 'UNPUBLISHED':
                courseHTML = courseHTML.replace("[courseStatus]", "Đã hủy công bố");
                break;
            default:
                courseHTML = courseHTML.replace("[courseStatus]", "Lưu trữ");
        }

        $('#course-list').append(courseHTML);

        if (course.courseStatus == 'DRAFT' || course.courseStatus == 'ARCHIVED') {
            let deleteCourseBtn = `
                <button class="dropdown-item focused cursor-pointer" 
                    onclick="deleteCourse('${course.id}')">Xóa khóa học
                </button>
                    `;
            $(`#course-menu-${course.id}`).append(deleteCourseBtn);
        }
        if (userRole == 'TEACHER') {
            if (course.courseStatus == 'DRAFT') {
                $(`#course-status-menu-${course.id}`).append(`<span>Yêu cầu xem xét khóa học</span>`);
            }
        } else if (userRole == "ADMIN") {
            switch (course.courseStatus) {
                case 'DRAFT':
                    $(`#course-status-menu-${course.id}`).append(`<span>Yêu cầu xem xét khóa học</span>`);
                    break;
                case 'WAITING_FOR_REVIEW':
                    $(`#course-status-menu-${course.id}`).append(`<span>Chấp thuận/ Từ chối khóa học</span>`);
                    break;
                case 'APPROVED':
                    $(`#course-status-menu-${course.id}`).append(`<span>Công bố khóa học</span>`);
                    break;
                case 'PUBLISHED':
                    $(`#course-status-menu-${course.id}`).append(`<span>Dừng công bố khóa học</span>`);
                    break;
                case 'REJECTED':
                case 'UNPUBLISHED':
                    $(`#course-status-menu-${course.id}`).append(`<span>Lưu trữ khóa học</span>`);
                    break;
                default:
                    $(`#course-status-menu-${course.id}`).append(`<span>Xem xét lại khóa học</span>`);
            }
        }
        for (let i = 0; i < course.trainingFields.length; i++) {
            let badgeClass = "";
            switch (course.trainingFields[i]) {
                case 'Bảo mật':
                    badgeClass = "badge-success";
                    break;
                case 'Đại cương':
                    badgeClass = "badge-warning";
                    break;
                case 'Lập trình':
                    badgeClass = "badge-primary";
                    break;
                default:
                    badgeClass = "badge-info";
            }
            let badgeHTML = `<span class="badge mr-1 ${badgeClass}">${course.trainingFields[i]}</span>`;
            $(`#training-field-badge-${course.id}`).append(badgeHTML);
        }

    });

    //render dữ liệu pagingable
    $("#pagination-container").empty();
    $("#pagination-container").append(`
                       <li class="page-item">
                          <a class="page-link cursor-pointer" id="double-left-page"><i class="fas fa-angle-double-left"></i></a>
                       </li>`);
    $("#pagination-container").append(`
                       <li class="page-item">
                           <a class="page-link cursor-pointer" id="left-page"><i class="fas fa-angle-left"></i></a>
                       </li>`);
    for (let i = 0; i < courseData.totalPage; i++) {
        const activeClass = i === pageIndex ? 'active' : '';
        $("#pagination-container").append(`
                        <li class="page-item ${activeClass}">
                            <a class="page-link cursor-pointer" id="page-link-${i}">${i + 1}</a>
                        </li>`);
    }
    $("#pagination-container").append(`
                       <li class="page-item">
                           <a class="page-link cursor-pointer" id="right-page"><i class="fas fa-angle-right"></i></a>
                       </li>`);
    $("#pagination-container").append(`
                       <li class="page-item">
                           <a class="page-link cursor-pointer" id="double-right-page"><i class="fas fa-angle-double-right"></i></a>
                       </li>`);

    // Gán sự kiện cho các nút phân trang
    $("#double-left-page").on("click", function (e) {
        e.preventDefault();
        searchCourses($("#course-page-size").val(), 0, "");
    });

    $("#left-page").on("click", function (e) {
        e.preventDefault();
        const currentPage = parseInt($(".pagination li.active a").text()) - 2;
        if (currentPage >= 0) {
            searchCourses($("#course-page-size").val(), currentPage, "");
        }
    });

    $("#right-page").on("click", function (e) {
        e.preventDefault();
        const currentPage = parseInt($(".pagination li.active a").text());
        if (currentPage < courseData.totalPage) {
            searchCourses($("#course-page-size").val(), currentPage, "");
        }
    });

    $("#double-right-page").on("click", function (e) {
        e.preventDefault();
        searchCourses($("#course-page-size").val(), courseData.totalPage - 1, "");
    });

    // Gán sự kiện cho từng nút trang
    for (let i = 0; i < courseData.totalPage; i++) {
        $("#page-link-" + i).on("click", function (e) {
            e.preventDefault();
            searchCourses($("#course-page-size").val(), i, "");
        });
    }
}

$(document).ready(() => {

    //thêm nội dung vào course modal nếu role là admin
    let adminData = `
                                <div class="col-xl-6 col-lg-6 col-md-12 col-sm-12 col-12">
                                    <div class="country-select mt-3">
                                        <label for="teacher">Giảng viên</label>
                                        <select class="w-100" id="teacher" name="teacher">

                                        </select>
                                    </div>
                                </div>
                                <div  class="col-xl-6 col-lg-6 col-md-12 col-sm-12 col-12" >
                                    <div class="country-select mt-3">
                                        <label for="discountCode">Mã giảm giá</label>
                                        <select class="w-100" id="discountCode" name="discountCode">

                                        </select>
                                    </div>
                                </div>`;

    const userInfo = JSON.parse(localStorage.getItem('user-info'));
    const userRole = userInfo ? userInfo.roles : null;
    if (userRole == 'ADMIN') {
        $('#course-common-data').append(adminData);
    }


    //set url mới khi thẻ select thay đổi
    $('#course-page-size').change(function (event) {
        const pageSize = event.target.value;
        searchCourses(pageSize, 0, '');
    });

    //xử lý gọi hàm searchCourses khi load trang web
    searchCourses(6, 0, "");

    //lấy dữ liệu đầu vào cho course modal
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
                if ($('#trainingFields').children().length === 0) {
                    if (!data || data.length === 0) {
                        return;
                    }
                    let trainingFieldOptions = "";
                    for (let i = 0; i < data.length; i++) {
                        trainingFieldOptions += "<option value='" + data[i].id + "'>" + data[i].fieldName + "</option>";
                    }
                    $('#trainingFields').append($(trainingFieldOptions));
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
                if ($('#discountCode').children().length === 0) {
                    if (!data || data.length === 0) {
                        return;
                    }
                    let discountCodeOptions = "<option value=''>--</option>";
                    for (let i = 0; i < data.length; i++) {
                        discountCodeOptions += "<option value='" + data[i].id + "'>" + data[i].codeName + "</option>";
                    }
                    $('#discountCode').append($(discountCodeOptions));
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
                if ($('#teacher').children().length === 0) {
                    if (!data || data.length === 0) {
                        return;
                    }
                    let teachersOptions = "";
                    for (let i = 0; i < data.length; i++) {
                        teachersOptions += "<option value='" + data[i].id + "'>" + data[i].user.fullName + "</option>";
                    }
                    $('#teacher').append($(teachersOptions));
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
        let trainingFields = $('#trainingFields').val();
        //chuyển dữ liệu từ object sang json
        const courseRequestBody = {};
        for (let i = 0; i < formCourseData.length; i++) {
            courseRequestBody[formCourseData[i].name] = formCourseData[i].value;
        }
        courseRequestBody['trainingFields'] = trainingFields;
        const actionType = $(event.currentTarget).attr("action-type");
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


});

//mở modal tạo mới khóa học
function createCourseBtn() {
    $('#course-modal #save-course-btn').attr("action-type", "CREATE");
    $('#course-modal #save-course-btn').prop("disabled", true);
    $('#course-modal').modal('show');
}

//mở modal khi ấn nút yêu cầu thay đổi trạng thái khóa học
function handleCourseStatus(id, courseStatus) {
    courseId = id;
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
}

//lấy dữ liệu từ backend và đổ lên form để update khóa học
async function updateCourse(id) {
    //call api lên backend để lấy dữ liệu
    const updateCourseId = id;
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
    $.each(course.trainingFields, function (index, trainingField) {
        $('#course-form #trainingFields').push(trainingField);
    });
    $('#course-form #teacher').val(course.teacherName);
    $('#course-form #discountCode').val(course.discountCodeName ? course.discountCodeName : '');

    $('#course-modal #save-course-btn').attr('action-type', "UPDATE");
    $('#course-modal #save-course-btn').attr("course-id", updateCourseId);
    $('#course-modal').modal("show");
}

//show modal to delete a course
function deleteCourse(id) {
    deleteCourseId = id;
    $('#course-delete-modal').modal('show');
}

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

function addSection(courseId) {
    localStorage.setItem("course-id", courseId);
    window.location.replace("http://localhost:8080/courses/" + courseId + "/sections");
}

//xử lý gọi hàm searchCourses khi người dùng nhấn enter ở form search
function handleSearchCourseFormSubmit() {
    let keyword = $('#search-course').val();
    searchCourses(6, 0, keyword);
    // return false;
}
