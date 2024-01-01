package com.quyen.hust.model.response;

import com.quyen.hust.statics.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {

    private Long id;

    private Double amount;

    private PaymentStatus paymentStatus;

    private LocalDateTime paymentDate;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private List<CourseResponse> courses;

}
