package com.quyen.hust.model.request.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quyen.hust.statics.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountCodeRequest {

    private Long id;

    @NotBlank(message = "Code name is required")
    @Length(max = 50, message = "Code name must be less than 50 characters")
    private String codeName;

    @NotNull(message = "Discount value is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount value must be positive number")
    private Double discountValue;

    @NotNull(message = "Discount unit is required")
    @Length(max = 50, message = "Discount unit must be less than 50 characters")
    private Unit discountUnit;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate endDate;

    @NotNull(message = "Usage limitation is required")
    @Min(value = 1, message = "Usage limitation must be greater than 1")
    private Integer usageLimitationCount;

//    @NotNull(message = "Used count is required")
//    @Min(value = 1, message = "Used count must be greater than 1")
//    private Integer usedCount;
//
//    private Boolean activated;

//    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
//    private LocalDateTime createdAt;
//
//    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
//    private LocalDateTime modifiedAt;

}
