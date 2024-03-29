package com.quyen.hust.model.request.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentRequest {

    private Long id;

    @NotNull(message = "User is required")
    @Min(value = 1, message = "User ID must be positive number")
    private Long userId;

    @NotNull(message = "Course is required")
    @Min(value = 1, message = "Course ID must be positive number")
    private Long courseId;

    @Min(value = 1, message = "Lesson ID must be positive number")
    private Long lessonID;

    @Min(value = 1, message = "Total lesson must be positive number")
    private Integer totalLesson;

    @Min(value = 1, message = "Completed lesson must be positive number")
    private Integer completedLesson;

    private Float completedRate;


}
