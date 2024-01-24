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
public class QuizRequest {
    private Long id;

    @NotBlank(message = "Title is required")
    @Length(max = 255, message = "Name must be less than 255 characters")
    private String title;

    @NotBlank(message = "Question is required")
    @Length(max = 1000, message = "Question must be less than 1000 characters")
    private String question;

    @Length(max = 1000, message = "Explanation must be less than 1000 characters")
    private String explanation;

    @NotNull(message = "Section is required")
    @Min(value = 1, message = "Section ID must be positive number")
    private Long sectionId;

//    private List<Answer> answers;


}
