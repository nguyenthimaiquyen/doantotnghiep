const userInfo = JSON.parse(localStorage.getItem('user-info'));
const userId = userInfo ? userInfo.id : null;

$(document).ready(function () {

    $('.collapse').each(function () {
        $(this).collapse('show');
    });

    var youtubeUrl = "https://www.googleapis.com/youtube/v3/videos?id=BFuaErJVhgk"
        + "&key=AIzaSyCWuCSRnm_IdsZgEPxi_Yg871zcfTCw5S8&part=snippet,contentDetails";
    $.ajax({
        async: false,
        type: 'GET',
        url: youtubeUrl,
        success: function (data) {
            var youtube_time = data.items[0].contentDetails.duration;
            var duration = formatISODate(youtube_time);
            if (ifr.next().is('.time')) {
                ifr.next().html(duration);
            }
        }
    });

    //lấy thời lượng video
    // $('video').on('loadedmetadata', function () {
    //     const duration = this.duration;
    //     //chuyển đổi thời lượng video sang định dạng phút giây
    //     const minutes = Math.floor(duration / 60);
    //     const seconds = Math.floor(duration % 60);
    //     const formattedDuration = minutes + ":" + (seconds < 10 ? '0' : '') + seconds;
    //     const url = window.location.href;
    //     const urlParts = url.split('/')
    //     const lessonId = urlParts[urlParts.length - 1];
    //     const requestBody = {};
    //     requestBody["videoDuration"] = formattedDuration;
    //     requestBody["lessonId"] = lessonId;
    //     $.ajax({
    //         url: "/api/v1/lessons/video-duration",
    //         type: "PUT",
    //         data: JSON.stringify(requestBody), //dữ liệu được gửi vào trong body của HTTP
    //         contentType: "application/json; charset=utf-8",
    //         success: function (data) {
    //         },
    //         error: function (err) {
    //             console.log(err)
    //         }
    //     });
    // });

    // $('iframe').on('loadedmetadata', function () {
    //     const duration = this.duration;
    //     //chuyển đổi thời lượng video sang định dạng phút giây
    //     const minutes = Math.floor(duration / 60);
    //     const seconds = Math.floor(duration % 60);
    //     const formattedDuration = minutes + ":" + (seconds < 10 ? '0' : '') + seconds;
    //     $('.video-duration').text(formattedDuration);
    // });

    //search course
    $('input[type="checkbox"], input[type="radio"], input[type="text"]').change(function () {
        let courseSearchRequestBody = {
            courseName: "",
            courseFee: [],
            teacherId: [],
            trainingFieldId: [],
            ratingValue: [],
            difficultyLevel: []
        };
        $('input[type="checkbox"]:checked, input[type="radio"]:checked').each(function () {
            const name = $(this).attr('name');
            const value = $(this).attr('id');
            switch (name) {
                case "course-fee":
                    courseSearchRequestBody.courseFee.push(parseFloat(value));
                    break;
                case "teacher":
                    courseSearchRequestBody.teacherId.push(parseInt(value));
                    break;
                case "training-field":
                    courseSearchRequestBody.trainingFieldId.push(parseInt(value));
                    break;
                case "rating":
                    courseSearchRequestBody.ratingValue.push(parseFloat(value));
                    break;
                case "difficultyLevel":
                    courseSearchRequestBody.difficultyLevel.push(value);
                    break;
                default:
                    break;
            }
        });
        $('input[type="text"]').each(function () {
            const value = $(this).val();
            courseSearchRequestBody.courseName = value;
        });
        callAPIToFilterCourse(courseSearchRequestBody, function (courseData) {
            appendCourse(courseData);
        });
    });

    function callAPIToFilterCourse(courseSearchRequestBody, callback) {
        $.ajax({
            url: "/api/v1/courses",
            type: 'GET',
            contentType: "application/json; charset=utf-8",
            data: courseSearchRequestBody,
            success: function (courseData) {
                callback(courseData);
            },
            error: function () {
                console.log("lỗi load dữ liệu course");
            }
        });
    }

    function appendCourse(courseData) {
        $('#result-count').empty();
        let resultHTML = `<span>Kết quả: ${courseData.totalElement} bản ghi</span>`;
        $('#result-count').append(resultHTML);
        $('#course-container').empty();
        $.each(courseData.courses, function (index, course) {
            let courseHTML = `
                <div class="col-xl-6 col-lg-6  col-md-6 col-sm-12 col-12">
                    <div class="single-popular-course zoom-img-hover">
                        <div class="position-relative">
                            <div class="p-course-img over-hidden position-relative">
                                <img class="w-100 h-200px img" src="/api/v1/files/${course.imageUrl}" alt="course-image">
                                <div class="pc-category z-index1 position-absolute">
                                     <span class="text-white f-500 theme-bg d-inline-block py-1 px-3">${course.trainingFields[0]}</span>                           
                                </div><!-- /pc-category  -->                            
                            </div><!-- /p-course img -->                            
                        </div>
                        <div class="popular-course-content-wrapper bg-white pt-25 pl-20 pr-20">
                             <div class="pc-info d-flex align-items-center justify-content-between pb-15">
                                  <ul class="review-ratting mr-800">
                                      <li>
                                          ${generateStarRating(course.rating)}                                                                    
                                      </li>                                                        
                                  </ul><!-- /review-ratting -->
                                  <p class="badge badge-primary ml-35">${formatDifficultyLevel(course.difficultyLevel)}</p>                          
                             </div>
                             <div class="single-popular-course-content primary-border-bottom pb-6">
                                 <h3><a class="focused cursor-pointer text-dark" onclick="showCourse(${course.id})">${course.title}</a></h3>                           
                                 <p class="admin mb-2"> Bởi <span class="theme-color f-500 d-inline-block">${course.teacherName}</span>
                                 </p>                           
                             </div><!-- /course-content -->
                             <div class="pc-info d-flex align-items-center justify-content-between pt-12 pb-15">
                                 <p class="pc-student mb-0"><span><i class="fal fa-user"></i></span>
                                     <span>${course.learnerCount}</span>
                                 </p>
                                 <div class="pc-price">
                                     <span class="primary-color f-500 d-inline-block">${formatCourseFee(course.courseFee)}</span>
                                     <span class="primary-color f-500 d-inline-block">${course.courseFeeUnit}</span>                           
                                 </div>                           
                             </div>                           
                        </div><!-- /popular-course-content-wrapper -->                            
                    </div><!-- /single-course -->                            
                </div><!-- /col -->`;
            $('#course-container').append(courseHTML);
        });
    }

    // Hàm để định dạng giá khóa học
    function formatCourseFee(courseFee) {
        return courseFee.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    }

    // Hàm để định dạng mức độ khó của khóa học
    function formatDifficultyLevel(difficultyLevel) {
        switch (difficultyLevel) {
            case "BEGINNER":
                return "Mới bắt đầu";
            case "INTERMEDIATE":
                return "Trung cấp";
            case "ADVANCED":
                return "Nâng cao";
            default:
                break;
        }
    }

    // Hàm tạo HTML cho đánh giá sao
    function generateStarRating(rating) {
        let starHTML = '';
        const fullStars = Math.floor(rating);
        const halfStar = rating % 1 !== 0;
        for (let i = 0; i < fullStars; i++) {
            starHTML += '<span><i class="fa fa-star"></i></span>';
        }
        if (halfStar) {
            starHTML += '<span><i class="fas fa-star-half-alt"></i></span>';
        }
        if (halfStar && (5 - 1 - fullStars > 0)) {
            for (let i = 0; i < (5 - 1 - fullStars); i++) {
                starHTML += '<span><i class="far fa-star"></i></span>';
            }
        }
        if (!halfStar && (5 - fullStars > 0)) {
            for (let i = 0; i < (5 - fullStars); i++) {
                starHTML += '<span><i class="far fa-star"></i></span>';
            }
        }
        return starHTML;
    }

    //chuyển hướng giao diện đến trang chi tiết khóa học
    $('.show-course-btn').click(async function (event) {
        let courseId = parseInt($(event.currentTarget).attr("course-id"));
        await showCourse(courseId);
    });

    //chuyển hướng giao diện đến trang chi tiết bài học
    $('.show-lesson-btn').click(function (event) {
        let courseId = parseInt($(event.currentTarget).attr("course-id"));
        let lessonId = parseInt($(event.currentTarget).attr("lesson-id"));
        window.location.replace("http://localhost:8080/users/" + userId + "/courses/" + courseId + "/lessons/" + lessonId);
    });

    //chuyển hướng giao diện đến trang chi tiết bài quiz
    $('.show-quiz-btn').click(function (event) {
        let courseId = parseInt($(event.currentTarget).attr("course-id"));
        let quizId = parseInt($(event.currentTarget).attr("quiz-id"));
        window.location.replace("http://localhost:8080/users/" + userId + "/courses/" + courseId + "/quizzes/" + quizId);
    });

    $('.check-answer').click(async function (event) {
        const quizId = parseInt($(event.currentTarget).attr("quiz-id"));
        const content = $('.answer-row .primary-border2.selected #contentAnswer').text();
        let quizData;
        await $.ajax({
            type: "GET",
            url: "/api/v1/quizzes/" + quizId,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                quizData = data;
            },
            error: function () {
                $.toast({
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    position: 'top-right',
                    bgColor: '#FF0000'
                })
            }
        });
        let selectedAnswer = null;
        $.each(quizData.answers, function (index, answer) {
            if (answer.content == content) {
                selectedAnswer = answer;
                return false;
            }
        });
        if (selectedAnswer && selectedAnswer.correct) {
            $.toast({
                text: 'Đáp án chính xác!',
                icon: 'success',
                position: 'mid-center',
                bgColor: '#4CAF50'
            });
            let explanation = `
                <label class="h6"> Giải thích:</label>
                <textarea class="primary-border2 form-control w-100" rows="5" readonly>${quizData.explanation}</textarea>`;
            $('#explanation').html(explanation);
            $('.answer-row .selected').removeClass("border border-primary");
            $('.answer-row .selected').addClass("bg-success");
        } else {
            $.toast({
                text: "Đáp án chưa chính xác, hãy thử lại nhé!",
                icon: 'warning',
                position: 'mid-center',
                bgColor: '#9EC600'
            });
            $('#explanation').empty();
            $('.answer-row .selected').removeClass("border border-primary");
            $('.answer-row .selected').addClass("bg-warning");
        }
    });

    $('.add-to-cart, .move-to-cart').click(async function (event) {
        const courseId = $(event.currentTarget).attr("course-id");
        const courseFee = $(event.currentTarget).attr("course-fee");
        const requestBody = {};
        requestBody["courseId"] = courseId;
        requestBody["userId"] = userId;
        if (courseFee == 0) {
            await enrollment(requestBody);
            return;
        }
        await $.ajax({
            type: "POST",
            url: "/api/v1/cartItems",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(requestBody), //dữ liệu được gửi vào trong body của HTTP
            success: function () {
                setTimeout(function () {
                    window.location.replace("http://localhost:8080/carts/" + userId);
                }, 500); //0.5s sau thì chuyển trang
            },
            error: function () {
                $.toast({
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    position: 'top-right',
                    bgColor: '#FF0000'
                })
            }
        });
    });

    async function enrollment(requestBody) {
        await $.ajax({
            type: "POST",
            url: "/api/v1/enrollments",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(requestBody), //dữ liệu được gửi vào trong body của HTTP
            success: function (enrollment) {
                window.location.replace("http://localhost:8080/users/" + userId + "/courses/" + enrollment.courseId + "/lessons/" + enrollment.lesson.id);
            },
            error: function () {
                $.toast({
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    position: 'top-right',
                    bgColor: '#FF0000'
                })
            }
        });

    }

    $('.add-to-wishlist, .move-to-wishlist').click(async function (event) {
        const courseId = $(event.currentTarget).attr("course-id");
        const requestBody = {};
        requestBody["courseId"] = courseId;
        requestBody["userId"] = userId;
        await $.ajax({
            type: "POST",
            url: "/api/v1/wishlistItems",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(requestBody), //dữ liệu được gửi vào trong body của HTTP
            success: function (data) {
                setTimeout(function () {
                    window.location.replace("http://localhost:8080/wishlists/" + userId);
                }, 500); //0.5s sau thì chuyển trang
            },
            error: function () {
                $.toast({
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    position: 'top-right',
                    bgColor: '#FF0000'
                })
            }
        });
    });

    $('.remove-cart-item').click(function (event) {
        const cartItemId = $(event.currentTarget).attr("cartItem-id");
        $.ajax({
            type: "DELETE",
            url: "/api/v1/cartItems/" + cartItemId,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                location.reload();
            },
            error: function () {
                $.toast({
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    position: 'top-right',
                    bgColor: '#FF0000'
                })
            }
        });
    });

    $('.remove-wishlistItem').click(function (event) {
        const wishlistItemId = $(event.currentTarget).attr("wishlistItem-id");
        $.ajax({
            type: "DELETE",
            url: "/api/v1/wishlistItems/" + wishlistItemId,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                location.reload();
            },
            error: function () {
                $.toast({
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    position: 'top-right',
                    bgColor: '#FF0000'
                })
            }
        });
    });

    $('.remove-all-cart-item').click(function (event) {
        const cartId = $(event.currentTarget).attr("cart-id");
        $.ajax({
            type: "DELETE",
            url: "/api/v1/carts/" + cartId + "/items",
            contentType: "application/json; charset=utf-8",
            success: function () {
                location.reload();
            },
            error: function () {
                $.toast({
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    position: 'top-right',
                    bgColor: '#FF0000'
                })
            }
        });
    });

    $(".preview-link").fancybox({
        type: 'iframe', // Loại nội dung là iframe
        iframe: {
            preload: false // Ngăn FancyBox tự động tải iframe khi được mở
        }
    });

    // $('.previous-lesson, .next-lesson').click(async function (event) {
    //     const courseId = $(event.currentTarget).attr("course-id");
    //     const lessonId = $(event.currentTarget).attr("lesson-id");
    //     const action = $(event.currentTarget).attr("action");
    //     $.ajax({
    //         type: "GET",
    //         url: "/api/v1/courses/" + courseId + "/lessons/" + lessonId,
    //         contentType: "application/json; charset=utf-8",
    //         data: {action: action},
    //         success: function (LessonInfo) {
    //             if (LessonInfo.lessonId) {
    //                 window.location.replace("http://localhost:8080/users/" + userId + "/courses/" + courseId + "/lessons/" + LessonInfo.lessonId);
    //             } else if (LessonInfo.quizId) {
    //                 window.location.replace("http://localhost:8080/courses/" + courseId + "/quizzes/" + LessonInfo.quizId);
    //             }
    //         },
    //         error: function () {
    //             $.toast({
    //                 text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
    //                 icon: 'error',
    //                 position: 'top-right',
    //                 bgColor: '#FF0000'
    //             })
    //         }
    //     });
    //
    // });


});

function highlightAnswer(answerElement) {
    let $selectedAnswerRow = $(answerElement).closest('.answer-row');
    $selectedAnswerRow.siblings().find('.form-control').removeClass('border border-primary selected bg-warning bg-success');
    $(answerElement).addClass('border border-primary selected');
}

async function showCourse(courseId) {
    let enrollmentData = {};
    await $.ajax({
        type: "GET",
        url: "/api/v1/enrollments/" + userId + "/" + courseId,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            enrollmentData = data;
        },
        error: function () {
            $.toast({
                text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                icon: 'error',
                position: 'top-right',
                bgColor: '#FF0000'
            })
        }
    });
    if (enrollmentData) {
        if (enrollmentData.lesson.id) {
            window.location.replace("http://localhost:8080/users/" + userId + "/courses/" + enrollmentData.courseId + "/lessons/" + enrollmentData.lesson.id);
        } else if (enrollmentData.quiz.id) {
            window.location.replace("http://localhost:8080/users/" + userId + "/courses/" + enrollmentData.courseId + "/quizzes/" + enrollmentData.quiz.id);
        }
    } else {
        window.location.replace("http://localhost:8080/courses/" + courseId);
    }
}
