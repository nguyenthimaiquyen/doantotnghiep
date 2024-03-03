package com.quyen.hust.service.user;

import com.quyen.hust.entity.course.Answer;
import com.quyen.hust.entity.course.Course;
import com.quyen.hust.entity.course.Section;
import com.quyen.hust.entity.user.Enrollment;
import com.quyen.hust.entity.user.User;
import com.quyen.hust.exception.CourseNotFoundException;
import com.quyen.hust.exception.UserNotFoundException;
import com.quyen.hust.model.request.user.EnrollmentRequest;
import com.quyen.hust.model.response.course.AnswerResponse;
import com.quyen.hust.model.response.course.LessonResponse;
import com.quyen.hust.model.response.course.QuizResponse;
import com.quyen.hust.model.response.user.EnrollmentResponse;
import com.quyen.hust.repository.course.AnswerJpaRepository;
import com.quyen.hust.repository.course.CourseJpaRepository;
import com.quyen.hust.repository.course.LessonJpaRepository;
import com.quyen.hust.repository.course.SectionJpaRepository;
import com.quyen.hust.repository.user.EnrollmentJpaRepository;
import com.quyen.hust.repository.user.UserJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EnrollmentService {
    private final EnrollmentJpaRepository enrollmentJpaRepository;
    private final CourseJpaRepository courseJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final AnswerJpaRepository answerJpaRepository;
    private final SectionJpaRepository sectionJpaRepository;
    private final LessonJpaRepository lessonJpaRepository;

    public EnrollmentResponse createEnrollment(EnrollmentRequest request) throws CourseNotFoundException, UserNotFoundException {
        Optional<Enrollment> existingEnrollmentOptional = enrollmentJpaRepository.findByCourseIdAndUserId(request.getCourseId(), request.getUserId());
        if (existingEnrollmentOptional.isPresent()) {
            return null;
        }
        Optional<Course> courseOptional = courseJpaRepository.findById(request.getCourseId());
        if (!courseOptional.isPresent()) {
            throw new CourseNotFoundException("Course with id " + request.getCourseId() + " could not be found!");
        }
        Optional<User> userOptional = userJpaRepository.findById(request.getUserId());
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("User with id " + request.getUserId() + " could not be found!");
        }
        List<Section> sections = sectionJpaRepository.findByCourseId(courseOptional.get().getId());
        Integer totalLessonAndQuiz = courseOptional.get().getTotalLessons() + courseOptional.get().getTotalQuizzes();
        Enrollment enrollment = Enrollment.builder()
                .course(courseOptional.get())
                .user(userOptional.get())
                .completedLesson(0)
                .completedRate(0f)
                .totalLessonAndQuiz(totalLessonAndQuiz)
                .lesson(lessonJpaRepository.findBySectionId(sections.get(0).getId()).get(0))
                .build();
        enrollmentJpaRepository.save(enrollment);
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .courseId(enrollment.getCourse().getId())
                .userId(enrollment.getUser().getId())
                .completedRate(enrollment.getCompletedRate())
                .totalLessonAndQuiz(enrollment.getTotalLessonAndQuiz())
                .completedLesson(enrollment.getCompletedLesson())
                .lesson(LessonResponse.builder()
                        .id(lessonJpaRepository.findBySectionId(sections.get(0).getId()).get(0).getId())
                        .build())
                .build();
    }

    public EnrollmentResponse getEnrollmentByUserIdAndCourseId(Long userId, Long courseId) {
        Optional<Enrollment> enrollmentOptional = enrollmentJpaRepository.findByCourseIdAndUserId(courseId, userId);
        if (!enrollmentOptional.isPresent()) {
            return null;
        }
//        List<Answer> answers = answerJpaRepository.findByQuizId(enrollmentOptional.get().getQuiz().getId());
//        List<AnswerResponse> answerResponses = answers.stream().map(
//                answer -> AnswerResponse.builder()
//                        .id(answer.getId())
//                        .isCorrect(answer.isCorrect())
//                        .content(answer.getContent())
//                        .quizId(answer.getId())
//                        .build()
//        ).collect(Collectors.toList());
        return EnrollmentResponse.builder()
                .id(enrollmentOptional.get().getId())
                .completedLesson(enrollmentOptional.get().getCompletedLesson())
                .completedRate(enrollmentOptional.get().getCompletedRate())
                .totalLessonAndQuiz(enrollmentOptional.get().getTotalLessonAndQuiz())
                .lesson(LessonResponse.builder()
                        .id(enrollmentOptional.get().getLesson().getId())
                        .title(enrollmentOptional.get().getLesson().getTitle())
                        .videoUrl(enrollmentOptional.get().getLesson().getVideoUrl())
                        .embeddedUrl(enrollmentOptional.get().getLesson().getEmbeddedUrl())
                        .videoDuration(enrollmentOptional.get().getLesson().getVideoDuration())
                        .content(enrollmentOptional.get().getLesson().getContent())
                        .fileUrl(enrollmentOptional.get().getLesson().getFileUrl())
                        .build())
//                .quiz(QuizResponse.builder()
//                        .id(enrollmentOptional.get().getQuiz().getId())
//                        .title(enrollmentOptional.get().getQuiz().getTitle())
//                        .timeCount(enrollmentOptional.get().getQuiz().getTimeCount())
////                        .answers(answerResponses)
//                        .question(enrollmentOptional.get().getQuiz().getQuestion())
//                        .explanation(enrollmentOptional.get().getQuiz().getExplanation())
//                        .build())
                .courseId(enrollmentOptional.get().getCourse().getId())
                .userId(enrollmentOptional.get().getUser().getId())
                .build();
    }


    public EnrollmentResponse getEnrollmentById(Long id) {
        Optional<Enrollment> enrollmentOptional = enrollmentJpaRepository.findById(id);
        List<Answer> answers = answerJpaRepository.findByQuizId(enrollmentOptional.get().getQuiz().getId());
        List<AnswerResponse> answerResponses = answers.stream().map(
                answer -> AnswerResponse.builder()
                        .id(answer.getId())
                        .isCorrect(answer.isCorrect())
                        .content(answer.getContent())
                        .quizId(answer.getId())
                        .build()
        ).collect(Collectors.toList());
        return EnrollmentResponse.builder()
                .id(enrollmentOptional.get().getId())
                .completedLesson(enrollmentOptional.get().getCompletedLesson())
                .completedRate(enrollmentOptional.get().getCompletedRate())
                .totalLessonAndQuiz(enrollmentOptional.get().getTotalLessonAndQuiz())
                .lesson(LessonResponse.builder()
                        .id(enrollmentOptional.get().getLesson().getId())
                        .title(enrollmentOptional.get().getLesson().getTitle())
                        .videoUrl(enrollmentOptional.get().getLesson().getVideoUrl())
                        .embeddedUrl(enrollmentOptional.get().getLesson().getEmbeddedUrl())
                        .videoDuration(enrollmentOptional.get().getLesson().getVideoDuration())
                        .content(enrollmentOptional.get().getLesson().getContent())
                        .fileUrl(enrollmentOptional.get().getLesson().getFileUrl())
                        .build())
                .quiz(QuizResponse.builder()
                        .id(enrollmentOptional.get().getQuiz().getId())
                        .title(enrollmentOptional.get().getQuiz().getTitle())
                        .timeCount(enrollmentOptional.get().getQuiz().getTimeCount())
                        .answers(answerResponses)
                        .question(enrollmentOptional.get().getQuiz().getQuestion())
                        .explanation(enrollmentOptional.get().getQuiz().getExplanation())
                        .build())
                .courseId(enrollmentOptional.get().getCourse().getId())
                .userId(enrollmentOptional.get().getUser().getId())
                .build();
    }
}
