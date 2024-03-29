package com.quyen.hust.model.request.course;

import com.quyen.hust.statics.DifficultyLevel;
import com.quyen.hust.statics.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequest {

    private Long id;

    @Min(value = 1, message = "Teacher ID must be positive number")
    private Long teacher;

    @Positive(message = "Discount ID must be positive number")
    private Long discountCode;

    @NotBlank(message = "Title is required")
    @Length(max = 255, message = "Title must be less than 255 characters")
    private String title;

    private String description;

    private String learningObjectives;

    @NotNull(message = "Course fee is required")
    private Double courseFee;

    @NotNull(message = "Course fee unit is required")
    private Unit courseFeeUnit;

    @NotNull(message = "Difficulty level is required")
    private DifficultyLevel difficultyLevel;

    @NotNull(message = "Training fields is required")
    private List<Long> trainingFields;

    public void setDiscountCode(String discountCode) {
        if (discountCode == null || discountCode.trim().isEmpty() || discountCode.trim().equals("\"\"")) {
            this.discountCode = null;
        } else {
            this.discountCode = Long.parseLong(discountCode);
        }
    }

}
