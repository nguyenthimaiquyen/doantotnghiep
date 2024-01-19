$(document).ready(() => {


    let deleteSectionId = -1;

    //2. chức năng thêm sửa xóa section

    //validate form thông tin chung của khóa học
    const sectionValidator = $('#section-form').validate({
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
                console.log(err)
                $.toast({
                    heading: 'Lỗi',
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    showHideTransition: 'fade',
                    position: 'top-right',
                    loader: false,
                    bgColor: '#FF0000'
                })
            }
        });

        if (!section) {
            $.toast({
                heading: 'Lỗi',
                text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                icon: 'error',
                showHideTransition: 'fade',
                position: 'top-right',
                loader: false,
                bgColor: '#FF0000'
            })
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
        const courseId = localStorage.getItem('course-id');
        sectionRequestBody["courseId"] = courseId;
        //call api lên backend
        $.ajax({
            url: "/api/v1/sections",
            type: method,
            data: JSON.stringify(sectionRequestBody),
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                $.toast({
                    heading: 'Thành công',
                    text: (method === "CREATE" ? "Tạo mới " : "Cập nhật ") + "thành công chương của khóa học!",
                    icon: 'success',
                    showHideTransition: 'fade',
                    position: 'top-right',
                    loader: false,
                    bgColor: '#4CAF50'
                })
                setTimeout(() => {
                    location.reload();
                }, 1000);
            },
            error: function (error) {
                $.toast({
                    heading: 'Lỗi',
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    showHideTransition: 'fade',
                    position: 'top-right',
                    loader: false,
                    bgColor: '#FF0000'
                })
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
                $.toast({
                    heading: 'Thành công',
                    text: 'Xóa chương thành công!',
                    icon: 'success',
                    showHideTransition: 'fade',
                    position: 'top-right',
                    loader: false,
                    bgColor: '#4CAF50'
                })
                setTimeout(() => {
                    location.reload();
                }, 1000);
            },
            error: function (err) {
                console.log(err)
                $.toast({
                    heading: 'Lỗi',
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    showHideTransition: 'fade',
                    position: 'top-right',
                    loader: false,
                    bgColor: '#FF0000'
                })
            }
        });
    });

    //reset form
    $('#section-modal').on('hidden.bs.modal', function () {
        $("#section-modal #save-section-btn").attr("action-type", "");
        $('#section-modal #save-section-btn').attr("section-id", "");
        $('#section-form').trigger("reset");
        $('#section-form input').removeClass("error");
        sectionValidator.resetForm();
    });


    //lựa chọn bài học
    $('.add-lesson-btn').click((event) => {
        $('#left-section-content').empty();
        const sectionTitle = $(event.currentTarget).attr("section-title");
        const sectionId = $(event.currentTarget).attr("section-id");
        let lessonOptions = `
                <div class="row mt-30 mb-100">
                    <h6 class="pt-20 pl-100">[sectionTitle]</h6>
                </div>
                <div class="row text-center mt-50">                
                    <div class="col-xl-6 col-lg-6 col-md-12 col-sm-12 col-12 pl-100">
                        <div class="" style="width:50%">
                            <button class="bg-white border-0 cursor-pointer" onclick="createLesson('${sectionId}', '${sectionTitle}')">
                                <div style="width:100%" class="text-center">
                                    <i class="far fa-edit fa-6x"></i>
                                </div>
                                <div class="text-center mt-4">
                                    <p>Tạo bài giảng</p>
                                </div>
                            </button>
                        </div>
                    </div>
                    <div class="col-xl-6 col-lg-6 col-md-12 col-sm-12 col-12 pl-0">
                        <div style="width:50%">
                            <button class="bg-white border-0 cursor-pointer" onclick="createTest('${sectionId}', '${sectionTitle}')">
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
        lessonOptions = lessonOptions.replace("[sectionTitle]", sectionTitle)
        $('#left-section-content').append(lessonOptions);
    });


});



function createLesson(sectionId, sectionTitle) {
    $('#left-section-content').empty();
    let lessonContent = `
                <div class="row mt-20">
                    <h6 class="pt-20 pl-100">[sectionTitle]</h6>
                </div>
                <div class="row mt-20 pl-50">
                     <div class="col-xl-9 col-lg-9 col-md-9 col-sm-9 col-9 pl-30">
                            <form id="lesson-title-form">
                                <input type="text" placeholder="Nhập tiêu đề bài học" class="form-control"
                                   id="title" name="title">
                            </form>
                     </div>
                     <div class="col-xl-3 col-lg-3 col-md-3 col-sm-3 col-3">
                            <button type="button" class="btn btn-primary btn-sm px-4 py-2" onclick="saveLessonContent('${sectionId}')">Lưu</button>
                     </div>
                </div>
                    <div class="row mt-30">
                        <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
                            <div class="faq2-wrapper pl-xl-4 pl-lg-3">
                                <form id="lesson-content-form">
                                          <div class="nav faq-nav justify-content-start" id="faq-tabs" role="tablist"
                                     aria-orientation="vertical">
                                    <a href="#tab1" class="nav-link active" data-toggle="pill" role="tab"
                                       aria-controls="tab1"
                                       aria-selected="true">Văn bản</a>
                                    <a href="#tab2" class="nav-link" data-toggle="pill" role="tab" aria-controls="tab2"
                                       aria-selected="false">Video</a>
                                    <a href="#tab3" class="nav-link" data-toggle="pill" role="tab" aria-controls="tab3"
                                       aria-selected="false">Đính kèm</a>
                                </div>
                                <div class="tab-content mt-55" id="faq-tab-content">
                                    <div class="tab-pane show active" id="tab1" role="tabpanel" aria-labelledby="tab1">
                                        <div class="row">
                                            <div class="col-xl-11 col-lg-11 col-md-11 col-sm-11 col-11">
                                                <div class="mb-3">
                                                    <textarea id="content" placeholder="Nhập nội dung bài học"
                                                              name="content"
                                                              class="primary-border2 w-100 pl-15 py-10 form-control"></textarea>
                                                </div>
                                            </div><!-- /col -->
                                        </div><!-- /row -->
                                    </div>
                                    <div class="tab-pane" id="tab2" role="tabpanel" aria-labelledby="tab2">
                                        <div class="row">
                                            <div class="col-xl-10 col-lg-10 col-md-12 col-sm-12 col-12">
                                                <div class="mb-40 mt-20">
                                                    <button class="btn btn-success f-500" id="video-upload-btn"> 
                                                        <i class="fas fa-upload"></i> Tải video lên
                                                    </button>
                                                    <input type="file" id="videoUrl" style="display:none;" accept="video/*">
                                                    <div id="video-status" class="my-3"></div>  
                                                </div>
                                                <div>
                                                    <label for="embeddedUrl">Hoặc nhập link nhúng youtube</label>
                                                    <textarea type="text" placeholder="Nhập link nhúng"
                                                           class="form-control form-control-lg"
                                                           id="embeddedUrl" name="embeddedUrl"></textarea>
                                                </div>
                                            </div><!-- /col -->
                                        </div><!-- /row -->
                                    </div>
                                    <div class="tab-pane" id="tab3" role="tabpanel" aria-labelledby="tab3">
                                        <div class="row">
                                            <div class="col-xl-6 col-lg-6 col-md-10 col-sm-10 col-12">
                                                <div class="custom-file mt-4">
                                                    <button class="btn btn-success f-500" id="file-upload-btn">
                                                        <i class="fas fa-upload"></i> Tải file lên
                                                    </button>
                                                    <input type="file" id="fileUrl" style="display:none;" accept="file/*">
                                                    <div id="file-status" class="my-3"></div>  
                                                </div>
                                                <div class="custom-file pmd-custom-file-filled mt-5 mb-60">
                                                    <span>Định dạng file được upload gồm: pdf, xls, xlsx, doc, docx, ppt, pptx.</span>
                                                    <span>Kích thước tối đa 50MB.</span>
                                                </div>
                                            </div><!-- /col -->
                                        </div><!-- /row -->
                                    </div>
                                </div>                      
                                </form>                               
                            </div><!-- /faq-wrapper -->
                        </div><!-- /col -->
                    </div><!-- /row -->
                    
                            
    `;
    lessonContent = lessonContent.replace("[sectionTitle]", sectionTitle)
    $('#left-section-content').append(lessonContent);

}

let chosenVideo = [];
let chosenFile = [];

$('#video-upload-btn').click(() => {
    $('#video-input').click();
});

$('#file-upload-btn').click(() => {
    $('#file-input').click();
});

$('#video-input').on("change", (event) => {
    const tempVideo = event.target.files;
    if (!tempVideo || tempVideo.length === 0) {
        return;
    }
    chosenVideo = tempVideo[0];
    if (chosenVideo) {
        $('#video-status').text('Đã tải lên: ' + chosenVideo.name);
    }
    const videoBlob = new Blob([chosenVideo], {type: chosenVideo.type});
    const videoUrl = URL.createObjectURL(videoBlob);
});

$('#file-input').on("change", (event) => {
    const tempFiles = event.target.files;
    if (!tempFiles || tempFiles.length === 0) {
        return;
    }
    chosenFile = tempFiles[0];
    if (chosenFile) {
        $('#file-status').text('Đã tải lên: ' + chosenFile.name);
    }
    const fileBlob = new Blob([chosenFile], {type: chosenFile.type});
    const fileUrl = URL.createObjectURL(fileBlob);
});

function saveLessonContent(sectionId) {
    //validate form thông tin chung của khóa học
    const lessonValidator = $('#lesson-title-form').validate({
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

    //validate lesson-title-form
    const isValidForm = $('#lesson-title-form').valid();
    if (!isValidForm) {
        return;
    }
    //lấy dữ liệu từ lesson-title-form
    const titleData = $('#lesson-title-form').serializeArray();
    if (!titleData || titleData.length === 0) {
        return;
    }
    //lấy dữ liệu từ lesson-content-form
    const contentData = $('#lesson-content-form').serializeArray();
    if (!contentData || contentData.length === 0) {
        return;
    }
    //chuyển dữ liệu từ dạng object sang json
    const requestBody = {};
    for (let i = 0; i < titleData.length; i++) {
        requestBody[titleData[i].name] = titleData[i].value;
    }
    for (let i = 0; i < contentData.length; i++) {
        requestBody[contentData[i].name] = contentData[i].value;
    }
    requestBody["sectionId"] = sectionId;
    const formData = new FormData();
    // formData.append("file", chosenFile, chosenFile.name);
    formData.append("lessonRequest", JSON.stringify(requestBody));
    //call api lên backend
    $.ajax({
        url: "/api/v1/lessons",
        type: "POST",
        data: formData, //dữ liệu được gửi vào trong body của HTTP
        contentType: false, //NEEDED, DON'T OMIT THIS
        processData: false, //NEEDED, DON'T OMIT THIS
        success: function (data) {
            $.toast({
                heading: 'Thành công',
                text: 'Tạo mới bài học thành công',
                icon: 'success',
                showHideTransition: 'fade',
                position: 'top-right',
                loader: false,
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
            });
            console.log(err)
        }
    });

}

