package com.quyen.hust.model.response.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
