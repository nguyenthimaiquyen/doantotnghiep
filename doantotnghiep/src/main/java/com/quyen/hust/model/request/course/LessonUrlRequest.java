package com.quyen.hust.model.request.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonUrlRequest {

    private Long id;

    @NotNull(message = "Lesson is required")
    @Min(value = 1, message = "Lesson ID must be positive number")
    private Long lessonID;

    @Length(max = 100, message = "Resource url must be less than 100 characters")
    private String resourceUrl;


}
