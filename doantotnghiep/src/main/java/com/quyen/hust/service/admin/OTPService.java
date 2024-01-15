package com.quyen.hust.service.admin;

import com.quyen.hust.entity.admin.OTP;
import com.quyen.hust.entity.user.User;
import com.quyen.hust.exception.UserNotFoundException;
import com.quyen.hust.repository.admin.OTPJpaRepository;
import com.quyen.hust.repository.user.UserJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class OTPService {
    private final OTPJpaRepository otpJpaRepository;
    private final UserJpaRepository userJpaRepository;

    private static final long OTP_EXPIRATION_MILLISECOND = 900000; //15 minutes

    public String createOTP(String email) {
        Random random = new Random();
        int newOTP = 10000 + random.nextInt(90000);
        User user = userJpaRepository.findByEmail(email);
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime = currentTime.plus(OTP_EXPIRATION_MILLISECOND, ChronoUnit.MILLIS);
        OTP otpData = OTP.builder()
                .otp(String.valueOf(newOTP))
                .liveTime(expirationTime)
                .user(user)
                .build();
        otpJpaRepository.save(otpData);
        return String.valueOf(newOTP);
    }


}
