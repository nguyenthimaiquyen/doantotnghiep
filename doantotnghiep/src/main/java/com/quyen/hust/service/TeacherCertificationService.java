package com.quyen.hust.service;

import com.quyen.hust.repository.TeacherCertificationJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TeacherCertificationService {
    private final TeacherCertificationJpaRepository teacherCertificationJpaRepository;

}
