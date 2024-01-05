package com.quyen.hust.repository.admin;

import com.quyen.hust.entity.admin.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTPJpaRepository extends JpaRepository<OTP, Long> {
}
