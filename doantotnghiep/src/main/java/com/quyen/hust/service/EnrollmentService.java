package com.quyen.hust.service;

import com.quyen.hust.repository.EnrollmentJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EnrollmentService {
    private final EnrollmentJpaRepository enrollmentJpaRepository;

}
