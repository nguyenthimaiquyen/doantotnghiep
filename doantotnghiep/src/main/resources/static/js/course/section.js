$(document).ready(() => {

    toastr.options.timeOut = 2500; // 2.5s

    let deleteSectionId = -1;

    //2. chức năng thêm sửa xóa section

    //validate form thông tin chung của khóa học
    const validator = $('#section-form').validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            'title': {
                required: true,
                maxlength: 255
            }
        },
        messages: {
            'title': {
                required: "Tiêu đề bắt buộc",
                maxlength: "Tiêu đề dài tối đa 255 ký tự"
            }
        }
    });

    //mở modal khi ấn thêm chương
    $('.create-section-btn').click(() => {
        console.log("vào hàm rồi")
        $('#section-modal #save-section-btn').attr("action-type", "CREATE");
        $('#section-modal').modal('show');
    });

    //mở modal khi ấn cập nhật chương
    $('.update-section-btn').click(async function (event) {
        //call api lên java để lấy dữ liệu
        const updateSectionId = parseInt($(event.currentTarget).attr("section-id"));
        let section = null;
        await $.ajax({
            url: "/api/v1/sections/" + updateSectionId,
            type: "GET",
            success: function (data) {
                section = data;
            },
            error: function (err) {
                toastr.warning(data.responseJSON.error);
                toastr.warning("Đã có lỗi xảy ra, vui lòng thử lại!");
            }
        });

        if (!section) {
            toastr.error("Đã có lỗi xảy ra, vui lòng thử lại!")
            return;
        }

        //đổ dữ liệu vào form
        $('#section-form #title').val(section.title);

        $('#section-modal #save-section-btn').attr('action-type', "UPDATE");
        $('#section-modal #save-section-btn').attr("section-id", updateSectionId);
        $('#section-modal').modal("show");
    });

    //create or update a section
    $('#save-section-btn').click(function (event) {
        //validate
        const isValidForm = $('#section-form').valid();
        if (!isValidForm) {
            return;
        }
        const actionType = $(event.currentTarget).attr("action-type");
        const sectionId = $(event.currentTarget).attr("section-id");
        //lấy dữ liệu từ form
        const formSectionData = $('#section-form').serializeArray();
        if (!formSectionData || formSectionData.length === 0) {
            return;
        }
        //chuyển dữ liệu từ object sang json
        const sectionRequestBody = {};
        for (let i = 0; i < formSectionData.length; i++) {
            sectionRequestBody[formSectionData[i].name] = formSectionData[i].value;
        }
        const method = actionType === "CREATE" ? "POST" : "PUT";
        if (method === "PUT") {
            sectionRequestBody["id"] = sectionId;
        }
        console.log(method)
        const courseId = localStorage.getItem('course-id');
        sectionRequestBody["courseId"] = courseId;
        console.log(sectionRequestBody)
        //call api lên backend
        $.ajax({
            url: "/api/v1/sections",
            type: method,
            data: JSON.stringify(sectionRequestBody),
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                toastr.success((method === "CREATE" ? "Tạo mới " : "Cập nhật ") + "thành công chương của khóa học!");
                setTimeout(() => {
                    location.reload();
                }, 1000);
            },
            error: function (error) {
                toastr.warning("Đã có lỗi xảy ra, vui lòng thử lại!");
            }
        });
        $("#section-modal #save-section-btn").attr("action-type", "");
        $('#section-modal #save-section-btn').attr("section-id", "");
    });

    //show modal to delete a section
    $('.delete-section-btn').click(function (event) {
        deleteSectionId = parseInt($(event.currentTarget).attr("section-id"));
        $('#section-delete-modal').modal('show');
    });

    //delete a section
    $('#delete-section-btn').click(function () {
        $.ajax({
            url: "/api/v1/sections/" + deleteSectionId,
            type: "DELETE",
            success: function (data) {
                toastr.success("Xóa chương thành công!");
                setTimeout(() => {
                    location.reload();
                }, 1000);
            },
            error: function (err) {
                toastr.warning("Đã có lỗi xảy ra, vui lòng thử lại!");
            }
        });
    });

    //reset form
    $('#section-modal').on('hidden.bs.modal', function () {
        $("#section-modal #save-section-btn").attr("action-type", "");
        $('#section-modal #save-section-btn').attr("section-id", "");
        $('#section-form').trigger("reset");
        $('#section-form input').removeClass("error");
        validator.resetForm();
    });

    //lựa chọn bài học
    $('.add-lesson-btn').click((event) => {
        event.stopPropagation();
        let lessonOptions = `
                <div class="row mt-30 mb-100">
                    <h6 class="pt-20">Tên chương</h6>
                </div>
                <div class="row text-center mt-50">
                    <div class="col-xl-6 col-lg-6 col-md-12 col-sm-12 col-12">
                        <div class="" style="width:50%">
                            <button class="bg-white border-0 cursor-pointer create-lesson-btn">
                                <div style="width:100%" class="text-center">
                                    <i class="far fa-edit fa-6x"></i>
                                </div>
                                <div class="text-center mt-4">
                                    <p>Tạo bài giảng</p>
                                </div>
                            </button>
                        </div>
                    </div>
                    <div class="col-xl-6 col-lg-6 col-md-12 col-sm-12 col-12">
                        <div style="width:50%">
                            <button class="bg-white border-0 cursor-pointer create-quiz-btn">
                                <div style="width:100%" class="text-center">
                                    <i class="far fa-question-circle fa-6x"></i>
                                </div>
                                <div class="text-center mt-4">
                                    <p>Tạo quiz</p>
                                </div>
                            </button>
                        </div>
                    </div>
                </div>
        `;
        $('#left-section-content').append(lessonOptions);
    });


});

