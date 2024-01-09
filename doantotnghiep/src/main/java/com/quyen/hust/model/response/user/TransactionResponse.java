package com.quyen.hust.model.response.user;

import com.quyen.hust.model.response.course.CourseResponse;
import com.quyen.hust.statics.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
