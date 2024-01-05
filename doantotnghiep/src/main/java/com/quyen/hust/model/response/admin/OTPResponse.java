package com.quyen.hust.model.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OTPResponse {

    private Long id;

    private String otp;

    private Long liveTime;

    private LocalDateTime createdAt;
}
