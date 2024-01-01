package com.quyen.hust.service;

import com.quyen.hust.repository.OTPJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OTPService {
    private final OTPJpaRepository otpJpaRepository;

}
