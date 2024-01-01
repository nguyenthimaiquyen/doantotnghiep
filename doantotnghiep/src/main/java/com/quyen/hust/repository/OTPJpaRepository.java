package com.quyen.hust.repository;

import com.quyen.hust.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTPJpaRepository extends JpaRepository<OTP, Long> {
}
