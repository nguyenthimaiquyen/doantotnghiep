package com.quyen.hust.model.response.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SectionResponse {

    private Long id;

    private String title;

    private List<LessonResponse> lessons;

    private List<QuizResponse> quizzes;

    private Integer totalLessons;

    private Integer totalQuizzes;

    private String totalTime;

}
