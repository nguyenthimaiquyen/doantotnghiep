let chosenVideo = [];
let chosenFile = [];
const maxFileSize = 52428800; //50MB

$(document).ready(() => {

    let deleteLessonId = -1;
    let deleteQuizId = -1;

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
                    <div class="col-xl-3 col-lg-3 col-md-12 col-sm-12 col-12">                       
                    </div>                
                    <div class="col-xl-3 col-lg-3 col-md-12 col-sm-12 col-12">
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
                    <div class="col-xl-3 col-lg-3 col-md-12 col-sm-12 col-12">
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
                    <div class="col-xl-3 col-lg-3 col-md-12 col-sm-12 col-12">                       
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

    //cập nhật lesson
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

    //show modal to delete a quiz
    $('.delete-quiz-btn').click(function (event) {
        deleteQuizId = parseInt($(event.currentTarget).attr("quiz-id"));
        $('#quiz-delete-modal').modal('show');
    });

    //delete a quiz
    $('#delete-quiz-btn').click(function () {
        $.ajax({
            url: "/api/v1/quizzes/" + deleteQuizId,
            type: "DELETE",
            success: function (data) {
                $.toast({
                    heading: 'Thành công',
                    text: "Xóa quiz thành công!",
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

    //cập nhật quiz
    $('.update-quiz-btn').click(async (event) => {
        const updateQuizId = parseInt($(event.currentTarget).attr("quiz-id"));
        let lessonData = null;
        await $.ajax({
            url: "/api/v1/quizzes/" + updateQuizId,
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
        createOrUpdateQuiz(lessonData.section.id, lessonData.section.title, lessonData);

    });

});


function createOrUpdateLesson(sectionId, sectionTitle, lessonData = {}) {
    $('#left-section-content').empty();
    let lessonContent = `
                <div class="row mt-15">
                    <div class="col-xl-9 col-lg-9 col-md-9 col-sm-9 col-9">
                        <h6 class="pt-20 pl-100">[sectionTitle]</h6>
                    </div>                   
                    <div class="col-xl-3 col-lg-3 col-md-3 col-sm-3 col-3 mt-10">
                        <button type="button" class="btn btn-primary btn-sm px-4 py-2" onclick="saveLessonContent('${sectionId}')">Lưu</button>
                    </div>                    
                </div>
                <div class="row mt-15 pl-50">
                     <div class="col-xl-9 col-lg-9 col-md-9 col-sm-9 col-9 pl-30">
                            <form id="lesson-title-form">
                                <label for="title" class="h6">Tiêu đề bài học<span class="text-danger">*</span></label>
                                <input type="text" placeholder="Nhập tiêu đề bài học" class="form-control"
                                   id="title" name="title">
                            </form>
                     </div>                 
                </div>
                <div class="row mt-20">
                        <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
                            <div class="faq2-wrapper pl-xl-4 pl-lg-3">
                                <form id="lesson-content-form">
                                    <div class="nav justify-content-start" id="faq-tabs" role="tablist"
                                        aria-orientation="vertical">
                                        <a href="#tab1" class="nav-link active" data-toggle="pill" role="tab"
                                           aria-controls="tab1"
                                           aria-selected="true">Văn bản</a>
                                        <a href="#tab2" class="nav-link" data-toggle="pill" role="tab" aria-controls="tab2"
                                           aria-selected="false">Video</a>
                                        <a href="#tab3" class="nav-link" data-toggle="pill" role="tab" aria-controls="tab3"
                                           aria-selected="false">Đính kèm</a>
                                    </div>
                                    <div class="tab-content mt-40" id="faq-tab-content">
                                        <div class="tab-pane show active" id="tab1" role="tabpanel" aria-labelledby="tab1">
                                            <div class="row">
                                                <div class="col-xl-11 col-lg-11 col-md-11 col-sm-11 col-11">
                                                    <div class="mb-3">
                                                        <label for="content" class="h6">Nội dung bài học</label>
                                                        <textarea id="content" placeholder="Nhập nội dung bài học" name="content"
                                                                  class="primary-border2 w-100 pl-15 py-10 form-control"></textarea>
                                                    </div>
                                                </div><!-- /col -->
                                            </div><!-- /row -->
                                        </div>
                                        <div class="tab-pane" id="tab2" role="tabpanel" aria-labelledby="tab2">
                                            <div class="row">
                                                <div class="col-xl-10 col-lg-10 col-md-12 col-sm-12 col-12">
                                                    <div class="mb-40 mt-15">
                                                        <button class="btn btn-success f-500" id="video-upload-btn"> 
                                                            <i class="fas fa-upload"></i> Tải video lên
                                                        </button>
                                                        <input type="file" id="videoUrl" style="display:none;" accept="video/*">
                                                        <div id="video-status" class="my-3"></div>  
                                                        <div id="video-size" class="text-danger my-3"></div>  
                                                    </div>
                                                    <div>
                                                        <label for="embeddedUrl" class="h6">Hoặc nhập link nhúng youtube</label>
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
                                                    <div class="custom-file mt-3">
                                                        <button class="btn btn-success f-500" id="file-upload-btn">
                                                            <i class="fas fa-upload"></i> Tải file lên
                                                        </button>
                                                        <input type="file" id="fileUrl" style="display:none;" accept=".pdf, .xls, .xlsx, .doc, .docx, .ppt, .pptx">
                                                        <div id="file-status" class="my-3"></div>  
                                                        <div id="file-size" class="my-3 text-danger"></div>  
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

    //đổ dữ liệu vào form
    $('#lesson-title-form #title').val(lessonData.title || '');
    $('#lesson-content-form #content').val(lessonData.content || '');
    $('#lesson-content-form #embeddedUrl').val(lessonData.embeddedUrl || '');

    if (lessonData.videoUrl) {
        $('#video-status').html('Đã lưu file: <span class="text-primary">' + lessonData.videoUrl + '</span>');
        $('#video-status').append("<button class=\"btn btn-sm ml-2 delete-video\"><i class=\"fas fa-times\"></i></button>")
        $('#video-status .delete-video').click((e) => {
            e.preventDefault();
            deleteVideo();
        });
    }
    if (lessonData.fileUrl) {
        $('#file-status').html('Đã lưu file: <span class="text-primary">' + lessonData.fileUrl + '</span>');
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
            $('#video-status').html('Đã tải lên: <span class="text-primary">' + chosenVideo.name + '</span>');
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
            $('#file-status').html('Đã tải lên: <span class="text-primary">' + chosenFile.name + '</span>');
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
    //validate lesson-title-form
    $('#lesson-title-form').validate({
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

    //validate lesson-content-form
    $('#lesson-content-form').validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            'content': {
                maxlength: 1000
            },
            "embeddedUrl": {
                maxlength: 1000
            }
        },
        messages: {
            'content': {
                maxlength: "Nội dung dài tối đa 1000 ký tự"
            },
            "embeddedUrl": {
                maxlength: "Link nhúng youtube dài tối đa 1000 ký tự"
            }
        }
    });

    //validate lesson-title-form
    const isValidTitleForm = $('#lesson-title-form').valid();
    if (!isValidTitleForm) {
        return;
    }
    const isValidContentForm = $('#lesson-content-form').valid();
    if (!isValidContentForm) {
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
        $('#videoUrl').val('');
        $('#video-status').empty();
        $('#video-size').empty();
    }
}

function deleteFile() {
    if (chosenFile) {
        chosenFile = null;
        $('#fileUrl').val('');
        $('#file-status').empty();
        $('#file-size').empty();
    }
}

function createOrUpdateQuiz(sectionId, sectionTitle, quizData = {}) {
    $('#left-section-content').empty();
    let quizContent = `
                <div class="row mt-15">
                    <div class="col-xl-9 col-lg-9 col-md-9 col-sm-9 col-9">
                        <h6 class="pt-20 pl-100">[sectionTitle]</h6>
                    </div>
                    <div class="col-xl-3 col-lg-3 col-md-3 col-sm-3 col-3 mt-10">
                        <button type="button" class="btn btn-primary btn-sm px-4 py-2"
                                onclick="saveQuizContent('${sectionId}')">Lưu
                        </button>
                    </div>
                </div>
                <div class="row mt-15 pl-50">
                    <div class="col-xl-11 col-lg-11 col-md-11 col-sm-11 col-11 pl-30">
                        <form id="quiz-title-form">
                            <label for="title" class="h6">Tiêu đề quiz<span class="text-danger">*</span></label>
                            <input type="text" placeholder="Nhập tiêu đề quiz" class="form-control"
                                   id="title" name="title">
                        </form>
                    </div>
                </div>
                <div class="row mt-20">
                    <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
                        <div class="faq2-wrapper pl-xl-4 pl-lg-3">
                            <form id="quiz-content-form">
                                <div class="row mt-2">
                                    <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12 mr-10">
                                        <label for="question" class="h6">Câu hỏi<span class="text-danger">*</span></label>
                                        <textarea id="question" placeholder="Nhập nội dung câu hỏi" name="question"
                                                  class="primary-border2 w-100 pl-15 form-control"></textarea>
                                    </div><!-- /col -->
                                </div><!-- /row -->
                                <div class="row mt-4">
                                    <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
                                        <label class="h6">Đáp án</label>
                                    </div><!-- /col -->
                                </div><!-- /row -->
                                <div class="row mt-2 answer-row" data-answer="1">
                                    <div class="col-xl-11 col-lg-11 col-md-11 col-sm-11 col-11">
                                        <input type="radio" class="form-check-input ml-15" name="answer1">
                                        <div class="input-group ml-5">
                                            <textarea id="answer1" name="answer1"
                                                      class="form-control primary-border2"></textarea>                                            
                                        </div>
                                    </div><!-- /col -->
                                </div><!-- /row -->
                                <div class="row mt-3 answer-row" data-answer="2">
                                    <div class="col-xl-11 col-lg-11 col-md-11 col-sm-11 col-11">
                                        <input type="radio" class="form-check-input ml-15" name="answer2">
                                        <div class="input-group ml-5">
                                            <textarea id="answer2" name="answer2"
                                                      class="form-control primary-border2"></textarea>
                                            <span onclick="removeAnswer(2)"><i class="fas fa-times-circle"></i></span>
                                        </div>
                                    </div><!-- /col -->
                                </div><!-- /row -->
                                <div class="row mt-3 answer-row" data-answer="3">
                                    <div class="col-xl-11 col-lg-11 col-md-11 col-sm-11 col-11">
                                        <input type="radio" class="form-check-input ml-15" name="answer3">
                                        <div class="input-group ml-5">
                                            <textarea id="answer3" name="answer3"
                                                      class="form-control primary-border2"></textarea>
                                            <span onclick="removeAnswer(3)"><i class="fas fa-times-circle"></i></span>
                                        </div>
                                    </div><!-- /col -->
                                </div><!-- /row -->
                                <div class="row mt-3 answer-row" data-answer="4">
                                    <div class="col-xl-11 col-lg-11 col-md-11 col-sm-11 col-11">
                                        <input type="radio" class="form-check-input ml-15" name="answer4">
                                        <div class="input-group ml-5">
                                            <textarea id="answer4" name="answer4"
                                                      class="form-control primary-border2"></textarea>
                                            <span onclick="removeAnswer(4)"><i class="fas fa-times-circle"></i></span>
                                        </div>
                                    </div><!-- /col -->
                                </div><!-- /row -->
                            </form>
                            <form id="explanation-form">
                                <div class="row mt-5 mb-50">
                                    <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12 mr-10 ml-0">
                                        <label for="explanation" class="h6">Giải thích</label>
                                        <textarea id="explanation" placeholder="Nhập nội dung giải thích"
                                            name="explanation" class="primary-border2 w-100 pl-15 form-control"></textarea>
                                    </div><!-- /col -->
                                </div><!-- /row -->
                            </form>
                            <div class="row mb-80">
                                <div class="col-lg-11">
                                    <div class="d-flex justify-content-end">
                                        <button class="btn btn-light bg-info f-500" onclick="addAnswer()">
                                            <i class="fas fa-plus mr-2"></i>Thêm đáp án
                                        </button>    
                                    </div><!-- /my-btn -->    
                                </div><!-- /col -->   
                            </div>                                
                        </div><!-- /faq-wrapper -->
                    </div><!-- /col -->
                </div><!-- /row -->                              
    `;
    quizContent = quizContent.replace("[sectionTitle]", sectionTitle)
    $('#left-section-content').append(quizContent);

    //đổ dữ liệu vào form
    $('#quiz-title-form #title').val(quizData.title || '');
    $('#quiz-content-form #question').val(quizData.question || '');
    $('#explanation-form #explanation').val(quizData.explanation || '');

    if (quizData.id) {
        localStorage.setItem("action-type", "UPDATE");
        localStorage.setItem("quiz-id", quizData.id);
    }
}

function saveQuizContent(sectionId) {
    //validate form thông tin chung của quiz
    $('#quiz-title-form').validate({
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
    //validate quiz-content-form
    $('#quiz-content-form').validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            'question': {
                required: true,
                maxlength: 1000
            }
        },
        messages: {
            'question': {
                required: "Câu hỏi bắt buộc",
                maxlength: "Nội dung câu hỏi dài tối đa 1000 ký tự"
            }
        }
    });
    //validate explanation-form
    $('#explanation-form').validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            'explanation': {
                maxlength: 1000
            }
        },
        messages: {
            'explanation': {
                maxlength: "Giải thích dài tối đa 1000 ký tự"
            }
        }
    });

    //validate quiz-title-form
    const isValidTitleForm = $('#quiz-title-form').valid();
    if (!isValidTitleForm) {
        return;
    }
    //validate quiz-content-form
    const isValidContentForm = $('#quiz-content-form').valid();
    if (!isValidContentForm) {
        return;
    }
    //validate lesson-title-form
    const isValidExplanationForm = $('#explanation-form').valid();
    if (!isValidExplanationForm) {
        return;
    }
    //lấy dữ liệu từ quiz-title-form
    const titleData = $('#quiz-title-form').serializeArray();
    if (!titleData || titleData.length === 0) {
        return;
    }
    //lấy dữ liệu từ lesson-content-form
    const contentData = $('#quiz-content-form').serializeArray();
    if (!contentData || contentData.length === 0) {
        return;
    }
    //lấy dữ liệu từ lesson-content-form
    const explanationData = $('#explanation-form').serializeArray();
    if (!explanationData || explanationData.length === 0) {
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
    for (let i = 0; i < explanationData.length; i++) {
        requestBody[explanationData[i].name] = explanationData[i].value;
    }

    const quizId = parseInt(localStorage.getItem("quiz-id"));
    const actionType = localStorage.getItem("action-type");
    const method = actionType === "UPDATE" ? "PUT" : "POST";
    if (method === "PUT") {
        requestBody["id"] = quizId;
    }
    requestBody["sectionId"] = sectionId;

    //call api lên backend
    $.ajax({
        url: "/api/v1/quizzes",
        type: method,
        data: JSON.stringify(requestBody), //dữ liệu được gửi vào trong body của HTTP
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            $.toast({
                heading: 'Thành công',
                text: (method === "PUT" ? "Cập nhật " : "Tạo mới ") + ' quiz thành công',
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
    localStorage.setItem("quiz-id", "");
}

function addAnswer() {
    let answerCount = $(".answer-row").length + 1;
    let newAnswerRow = $(".answer-row:first").clone();
    newAnswerRow.attr("data-answer", answerCount);
    newAnswerRow.find("input[type=radio]").prop("checked", false);
    newAnswerRow.find("textarea").val("");
    newAnswerRow.append('<span class="remove-answer" onclick="removeAnswer(' + answerCount + ')"><i class="fas fa-times-circle"></i></span>');
    $("#quiz-content-form").append(newAnswerRow);
}

function removeAnswer(answerNumber) {
    $(".answer-row[data-answer=" + answerNumber + "]").remove();
}