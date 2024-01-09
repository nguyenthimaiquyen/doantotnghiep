package com.quyen.hust.entity.admin;

import com.quyen.hust.entity.BaseEntity;
import com.quyen.hust.statics.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "discount_codes")
public class DiscountCode extends BaseEntity {

    @Column(length = 50, name = "code_name")
    private String codeName;

    @Column(name = "discount_value")
    private Double discountValue;

    @Column(length = 50, name = "discount_unit")
    @Enumerated(EnumType.STRING)
    private Unit discountUnit;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "usage_limitation_count")
    private Integer usageLimitationCount;

    @Column(name = "used_count")
    private Integer usedCount;

    @Column
    private Boolean activated;

}
