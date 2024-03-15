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
public class QuizResponse {
    private Long id;

    private String title;

    private String question;

    private String explanation;

    private List<AnswerResponse> answers;

    private Long sectionId;

    private String sectionTitle;

    private String timeCount;

    private Long previousQuizId;

    private Long nextQuizId;

    private Long previousLessonId;

    private Long nextLessonId;

}
