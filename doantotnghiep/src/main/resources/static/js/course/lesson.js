let chosenVideo = [];
let chosenFile = [];
const maxFileSize = 20971520; //20MB

$(document).ready(() => {

    let deleteLessonId = -1;

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
                            <button class="bg-white border-0 cursor-pointer" onclick="createOrUpdateLesson('${sectionId}', '${sectionTitle}')">
                                <div style="width:100%" class="text-center">
                                    <i class="far fa-edit fa-5x"></i>
                                </div>
                                <div class="text-center mt-4">
                                    <p>Tạo bài giảng</p>
                                </div>
                            </button>
                        </div>
                    </div>
                    <div class="col-xl-6 col-lg-6 col-md-12 col-sm-12 col-12 pl-0">
                        <div style="width:50%">
                            <button class="bg-white border-0 cursor-pointer" onclick="createOrUpdateTest('${sectionId}', '${sectionTitle}')">
                                <div style="width:100%" class="text-center">
                                    <i class="far fa-question-circle fa-5x"></i>
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

    //show modal to delete a lesson
    $('.delete-lesson-btn').click(function (event) {
        deleteLessonId = parseInt($(event.currentTarget).attr("lesson-id"));
        $('#lesson-delete-modal').modal('show');
    });

    //delete a lesson
    $('#delete-lesson-btn').click(function () {
        $.ajax({
            url: "/api/v1/lessons/" + deleteLessonId,
            type: "DELETE",
            success: function (data) {
                $.toast({
                    heading: 'Thành công',
                    text: "Xóa bài học thành công!",
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

    //cập nhật bài học
    $('.update-lesson-btn').click(async (event) => {
        const updateLessonId = parseInt($(event.currentTarget).attr("lesson-id"));
        let lessonData = null;
        await $.ajax({
            url: "/api/v1/lessons/" + updateLessonId,
            type: "GET",
            success: function (data) {
                lessonData = data;
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
        if (!lessonData) {
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
        createOrUpdateLesson(lessonData.section.id, lessonData.section.title, lessonData);

    });


});


function createOrUpdateLesson(sectionId, sectionTitle, lessonData = {}) {
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
                                                        <div id="video-size" class="text-danger my-3"></div>  
                                                    </div>
                                                    <div>
                                                        <label for="embeddedUrl">Hoặc nhập link nhúng youtube</label>
                                                        <textarea type="text" placeholder="Nhập link nhúng"
                                                               class="form-control form-control-lg" rows="6"
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
                                                        <div id="file-size" class="my-3 text-danger"></div>  
                                                    </div>
                                                    <div class="custom-file pmd-custom-file-filled mt-5 mb-60">
                                                        <span>Định dạng file được upload gồm: pdf, xls, xlsx, doc, docx, ppt, pptx.</span>
                                                        <span>Kích thước tối đa 20MB.</span>
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

    //đổ dữ liệu vào form
    $('#lesson-title-form #title').val(lessonData.title || '');
    $('#lesson-content-form #content').val(lessonData.content || '');
    $('#lesson-content-form #embeddedUrl').val(lessonData.embeddedUrl || '');
    if (lessonData.videoUrl) {
        $('#video-status').html('Đã lưu file: ' + lessonData.videoUrl);
        $('#video-status').append("<button class=\"btn btn-sm ml-2 delete-video\"><i class=\"fas fa-times\"></i></button>")
        $('#video-status .delete-video').click((e) => {
            e.preventDefault();
            deleteVideo();
        });
    }
    if (lessonData.fileUrl) {
        $('#file-status').html('Đã lưu file: ' + lessonData.fileUrl);
        $('#file-status').append("<button class=\"btn btn-sm ml-2 delete-file\"><i class=\"fas fa-times\"></i></button>")
        $('#file-status .delete-file').click((e) => {
            e.preventDefault();
            deleteFile();
        });
    }

    $('#video-upload-btn').click((event) => {
        event.preventDefault();
        $('#videoUrl').click();
    });

    $('#file-upload-btn').click((event) => {
        event.preventDefault();
        $('#fileUrl').click();
    });

    $('#videoUrl').on("change", (event) => {
        const tempVideo = event.target.files;
        if (!tempVideo || tempVideo.length === 0) {
            return;
        }
        chosenVideo = tempVideo[0];
        if (chosenVideo) {
            $('#video-status').html('Đã tải lên: ' + chosenVideo.name);
            $('#video-status').append("<button class=\"btn btn-sm ml-2 delete-video\"><i class=\"fas fa-times\"></i></button>")
            $('#video-status .delete-video').click((e) => {
                e.preventDefault();
                deleteVideo();
            });
        }
        if (chosenVideo.size > maxFileSize) {
            $('#video-size').text("Kích thức video vượt quá giới hạn cho phép!")
        }
    });

    $('#fileUrl').on("change", (event) => {
        const tempFiles = event.target.files;
        if (!tempFiles || tempFiles.length === 0) {
            return;
        }
        chosenFile = tempFiles[0];
        if (chosenFile) {
            $('#file-status').text('Đã tải lên: ' + chosenFile.name);
            $('#file-status').append("<button class='btn btn-sm ml-2 delete-file'><i class=\"fas fa-times\"></i></button>")
            $('#file-status .delete-file').click((e) => {
                e.preventDefault();
                deleteFile();
            });
        }
        if (chosenFile.size > maxFileSize) {
            $('#file-size').text("Kích thức file vượt quá giới hạn cho phép!")
        }
    });

    if (lessonData.id) {
        localStorage.setItem("action-type", "UPDATE");
        localStorage.setItem("lesson-id", lessonData.id);
    }

}

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

    const lessonId = parseInt(localStorage.getItem("lesson-id"));
    const actionType = localStorage.getItem("action-type");
    const method = actionType === "UPDATE" ? "PUT" : "POST";
    if (method === "PUT") {
        requestBody["id"] = lessonId;
    }
    requestBody["sectionId"] = sectionId;

    //tạo 1 blob từ dữ liệu JSONs
    const jsonBlob = new Blob([JSON.stringify(requestBody)], {
        type: "application/json; charset=utf-8"
    });
    //sử dụng formData để đóng gói dữ liệu
    const formData = new FormData();
    formData.append("file", chosenFile);
    formData.append("video", chosenVideo);
    formData.append("lessonRequest", jsonBlob);
    //call api lên backend
    $.ajax({
        url: "/api/v1/lessons",
        type: method,
        data: formData, //dữ liệu được gửi vào trong body của HTTP
        contentType: false, //NEEDED, DON'T OMIT THIS
        processData: false, //NEEDED, DON'T OMIT THIS
        success: function (data) {
            $.toast({
                heading: 'Thành công',
                text: (method === "PUT" ? "Cập nhật " : "Tạo mới ") + 'bài học thành công',
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
    localStorage.setItem("action-type", "");
    localStorage.setItem("lesson-id", "");
}

function deleteVideo() {
    if (chosenVideo) {
        chosenVideo = null;
        $('#video-status').empty();
        $('#video-size').empty();
    }
}

function deleteFile() {
    if (chosenFile) {
        chosenFile = null;
        $('#file-status').empty();
        $('#file-size').empty();
    }
}

function createOrUpdateTest() {

}