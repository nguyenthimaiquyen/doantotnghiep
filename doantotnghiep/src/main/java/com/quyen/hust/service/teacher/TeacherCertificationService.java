package com.quyen.hust.service.teacher;

import com.quyen.hust.repository.teacher.TeacherCertificationJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TeacherCertificationService {
    private final TeacherCertificationJpaRepository teacherCertificationJpaRepository;

}
