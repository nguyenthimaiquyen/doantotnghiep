package com.quyen.hust.model.request.teacher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRequest {

    private Long id;

    @NotNull(message = "User is required")
    @Min(value = 1, message = "User ID must be positive number")
    private Long userId;

    @NotBlank(message = "Teaching expertise is required")
    @Length(max = 100, message = "Teaching expertise must be less than 100 characters")
    private String teachingExpertise;

    @NotNull(message = "Years of experience is required")
    @Pattern(regexp = "^[1-9]\\d*$", message = "Years of experience must be digits")
    private Integer yearsOfExperience;
}
