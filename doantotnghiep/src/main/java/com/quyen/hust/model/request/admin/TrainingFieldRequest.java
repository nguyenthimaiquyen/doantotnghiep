package com.quyen.hust.model.request.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

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
