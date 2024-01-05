package com.quyen.hust.model.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificationResponse {

    private Long id;

    private LocalDate issueDate;

    private LocalDate effectiveDate;

    private LocalDate expirationDate;

    private String certificateUrl;
}
