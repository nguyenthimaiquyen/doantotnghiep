package com.quyen.hust.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quyen.hust.entity.TrainingField;
import com.quyen.hust.statics.DifficultyLevel;
import com.quyen.hust.statics.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequest {

    private Long id;

    @NotNull(message = "Teacher is required")
    @Min(value = 1, message = "Teacher ID must be positive number")
    private Long teacherID;

    @Min(value = 1, message = "Discount ID must be positive number")
    private Long discountID;

    @NotBlank(message = "Title is required")
    @Length(max = 255, message = "Title must be less than 255 characters")
    private String title;

    private String description;

    private String learningObjectives;

    @NotNull(message = "Course fee is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Course fee must be positive number")
    private Double courseFee;

    @NotBlank(message = "Course fee unit is required")
    @Length(max = 20, message = "Course fee unit must be less than 20 characters")
    private String courseFeeUnit;

    @NotNull(message = "Status is required")
    private Status status;

    @Pattern(regexp = "^[1-9]\\d*$", message = "The total lessons must be in digits")
    private Integer totalLessons;

    @Pattern(regexp = "^[1-9]\\d*$", message = "The total video duration must be in digits")
    private Long totalVideoDuration;

    @Pattern(regexp = "^[1-9]\\d*$", message = "The total test must be in digits")
    private Long totalTest;

    @NotNull(message = "Difficulty level is required")
    @Length(max = 50, message = "Difficulty level must be less than 50 characters")
    private DifficultyLevel difficultyLevel;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDateTime modifiedAt;

    @NotNull(message = "Training fields is required")
    private List<TrainingField> trainingFields;

}
