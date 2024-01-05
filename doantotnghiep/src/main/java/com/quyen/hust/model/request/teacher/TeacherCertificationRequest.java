package com.quyen.hust.model.request.teacher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherCertificationRequest {

    private Long id;

    @NotNull(message = "Teacher is required")
    @Min(value = 1, message = "Teacher ID must be positive number")
    private Long teacherID;

    @NotBlank(message = "Certification is required")
    private String certification;


}
