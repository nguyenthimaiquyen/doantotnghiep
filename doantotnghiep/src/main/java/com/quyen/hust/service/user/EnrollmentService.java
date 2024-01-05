package com.quyen.hust.service.user;

import com.quyen.hust.repository.user.EnrollmentJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EnrollmentService {
    private final EnrollmentJpaRepository enrollmentJpaRepository;

}
