$(document).ready(() => {

    let deleteQuizId = -1;

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
                    text: "Xóa quiz thành công!",
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

    //cập nhật quiz
    $('.update-quiz-btn').click(async (event) => {
        const updateQuizId = parseInt($(event.currentTarget).attr("quiz-id"));
        let Data = null;
        await $.ajax({
            url: "/api/v1/quizzes/" + updateQuizId,
            type: "GET",
            success: function (data) {
                Data = data;
            },
            error: function (err) {
                console.log(err)
                $.toast({
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    position: 'top-right',
                    bgColor: '#FF0000'
                })
            }
        });
        if (!Data) {
            $.toast({
                text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                icon: 'error',
                position: 'top-right',
                bgColor: '#FF0000'
            })
            return;
        }
        createOrUpdateQuiz(Data.sectionId, Data.sectionTitle, Data);

    });

});

function createOrUpdateQuiz(sectionId, sectionTitle, quizData = {}) {
    localStorage.setItem("action-type", "");
    localStorage.setItem("quiz-id", "");
    localStorage.setItem("lesson-id", "");
    $('#left-section-content').empty();
    let quizContent = `
                <div class="row mt-15">
                    <div class="col-xl-9 col-lg-9 col-md-9 col-sm-9 col-9">
                        <h6 class="pt-20 pl-100">Chương: [sectionTitle]</h6>
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
                                        <textarea id="question" placeholder="Nhập nội dung câu hỏi" name="question" rows="5"
                                                  class="primary-border2 w-100 pl-15 form-control"></textarea>
                                    </div><!-- /col -->
                                </div><!-- /row -->
                                <div class="row mt-4">
                                    <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
                                        <label class="h6">Đáp án<span class="text-danger">*</span></label>
                                    </div><!-- /col -->
                                </div><!-- /row -->
                                <div class="row mt-2 answer-row" data-answer="1">
                                    <div class="col-xl-11 col-lg-11 col-md-11 col-sm-11 col-11">
                                        <input type="radio" class="form-check-input ml-15" name="answer" id="answer-radio-1">
                                        <div class="input-group ml-5">
                                            <textarea id="answer1" name="answer1" rows="3" placeholder="Nhập đáp án"
                                                      class="form-control primary-border2"></textarea>                                            
                                        </div>
                                    </div>
                                </div>
                                <div class="row mt-3 answer-row" data-answer="2">
                                    <div class="col-xl-11 col-lg-11 col-md-11 col-sm-11 col-11">
                                        <input type="radio" class="form-check-input ml-15" name="answer" id="answer-radio-2">
                                        <div class="input-group ml-5">
                                            <textarea id="answer2" name="answer2" rows="3" placeholder="Nhập đáp án"
                                                      class="form-control primary-border2"></textarea>
                                            <span onclick="removeAnswer(2)" class="focused cursor-pointer">
                                                <i class="fas fa-times-circle"></i></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="row mt-3 answer-row" data-answer="3">
                                    <div class="col-xl-11 col-lg-11 col-md-11 col-sm-11 col-11">
                                        <input type="radio" class="form-check-input ml-15" name="answer" id="answer-radio-3">
                                        <div class="input-group ml-5">
                                            <textarea id="answer3" name="answer3" rows="3" placeholder="Nhập đáp án"
                                                      class="form-control primary-border2"></textarea>
                                            <span onclick="removeAnswer(3)" class="focused cursor-pointer">
                                                <i class="fas fa-times-circle"></i></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="row mt-3 answer-row" data-answer="4">
                                    <div class="col-xl-11 col-lg-11 col-md-11 col-sm-11 col-11">
                                        <input type="radio" class="form-check-input ml-15" name="answer" id="answer-radio-4">
                                        <div class="input-group ml-5">
                                            <textarea id="answer4" name="answer4" rows="3" placeholder="Nhập đáp án"
                                                      class="form-control primary-border2"></textarea>
                                            <span onclick="removeAnswer(4)" class="focused cursor-pointer">
                                                <i class="fas fa-times-circle"></i></span>
                                        </div>
                                    </div>
                                </div>                              
                            </form>
                            <form id="explanation-form">
                                <div class="row mt-5 mb-50">
                                    <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12 mr-10 ml-0">
                                        <label for="explanation" class="h6">Giải thích</label>
                                        <textarea id="explanation" placeholder="Nhập nội dung giải thích" rows="5"
                                            name="explanation" class="primary-border2 w-100 pl-15 form-control"></textarea>
                                    </div>
                                </div>
                            </form>
                            <div class="row mb-80">
                                <div class="col-lg-11">
                                    <div class="d-flex justify-content-end">
                                        <button class="btn btn-light bg-info f-500 focused cursor-pointer" onclick="addAnswer()">
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
    //đổ dữ liệu vào các answer
    let answerCount = quizData.answers && quizData.answers.length ? quizData.answers.length : 0;
    if (quizData.answers) {
        $('.answer-row').remove();
    }
    for (let i = 0; i < answerCount; i++) {
        addAnswer();
        $(`#quiz-content-form #answer${i+1}`).val(quizData.answers[i].content || '');
        //chưa xử lý được radio là checked khi đáp án đó là đúng
        if (quizData.answers[i].correct) {
            $(`#quiz-content-form #answer-radio-${i+1}`).prop('checked', true);
        }
    }
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
    requestBody[contentData[0].name] = contentData[0].value;
    for (let i = 0; i < explanationData.length; i++) {
        requestBody[explanationData[i].name] = explanationData[i].value;
    }
    let answerList = [];
    $('.answer-row').each(function () {
        let radio = $(this).find('input[type="radio"]');
        let textarea = $(this).find('textarea');
        let data;
        if (radio.prop("checked")) {
            data = {
                content: textarea.val(),
                isCorrect: true
            }
        } else {
            data = {
                content: textarea.val(),
                isCorrect: false
            }
        }
        answerList.push(data);
    });
    requestBody["answers"] = answerList;
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
                text: (method === "PUT" ? "Cập nhật " : "Tạo mới ") + ' quiz thành công',
                icon: 'success',
                position: 'top-right',
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
            });
            console.log(err)
        }
    });
    localStorage.setItem("action-type", "");
    localStorage.setItem("quiz-id", "");
}

function addAnswer() {
    let answerCount = $(".answer-row").length + 1;
    let newAnswerRow = `
        <div class="row mt-3 answer-row" data-answer="${answerCount}">
            <div class="col-xl-11 col-lg-11 col-md-11 col-sm-11 col-11">
                 <input type="radio" class="form-check-input ml-15" name="answer" id="answer-radio-${answerCount}">
                 <div class="input-group ml-5">
                     <textarea id="answer${answerCount}" name="answer${answerCount}" placeholder="Nhập đáp án"
                         rows="3" class="form-control primary-border2"></textarea>
                     <span onclick="removeAnswer(${answerCount})" class="focused cursor-pointer"><i class="fas fa-times-circle"></i></span>                 
                 </div>                                         
            </div>                 
        </div>
    `;
    $("#quiz-content-form").append(newAnswerRow);
}

function removeAnswer(answerNumber) {
    $(".answer-row[data-answer=" + answerNumber + "]").remove();
}