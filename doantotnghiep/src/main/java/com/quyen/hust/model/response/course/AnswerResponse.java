package com.quyen.hust.model.response.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerResponse {

    private Long id;

    private String content;

    private boolean isCorrect;

    private Long quizId;

}
