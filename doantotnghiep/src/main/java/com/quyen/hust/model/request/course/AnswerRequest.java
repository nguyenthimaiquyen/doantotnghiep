package com.quyen.hust.model.request.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerRequest {

    private Long id;

    @NotBlank(message = "Content is required")
    @Length(max = 255, message = "Content must be less than 255 characters")
    private String content;

    private boolean isCorrect;

    @NotNull(message = "Quiz is required")
    @Min(value = 1, message = "Quiz ID must be positive number")
    private Long quizId;


}
