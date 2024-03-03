package com.quyen.hust.model.request.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainingFieldRequest {

    private Long id;

    @NotBlank(message = "Field name is required")
    @Length(max = 100, message = "Field name must be less than 100 characters")
    private String fieldName;

    @Length(max = 255, message = "Field name must be less than 255 characters")
    private String description;


}
