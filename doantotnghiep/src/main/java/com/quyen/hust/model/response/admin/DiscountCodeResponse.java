package com.quyen.hust.model.response.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quyen.hust.statics.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscountCodeResponse {

    private Long id;

    private String codeName;

    private Double discountValue;

    private Unit discountUnit;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private Integer usageLimitationCount;

    private Integer usedCount;

    private Boolean activated;


}
