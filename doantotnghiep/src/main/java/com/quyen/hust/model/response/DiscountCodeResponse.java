package com.quyen.hust.model.response;

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

    private String discountUnit;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer usageLimitationCount;

    private Integer usedCount;

    private Boolean activated;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
