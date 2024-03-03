package com.quyen.hust.model.response.user;

import com.quyen.hust.model.response.course.LessonResponse;
import com.quyen.hust.model.response.course.QuizResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentResponse {

    private Long id;

    private Integer totalLessonAndQuiz;

    private Integer completedLesson;

    private Float completedRate;

    private LessonResponse lesson;

    private QuizResponse quiz;

    private Long courseId;

    private Long userId;
}
