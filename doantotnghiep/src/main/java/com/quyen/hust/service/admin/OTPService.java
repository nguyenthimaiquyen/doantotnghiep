package com.quyen.hust.service.admin;

import com.quyen.hust.repository.admin.OTPJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OTPService {
    private final OTPJpaRepository otpJpaRepository;

}
