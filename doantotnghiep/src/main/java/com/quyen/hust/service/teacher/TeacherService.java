package com.quyen.hust.service.teacher;

import com.quyen.hust.repository.teacher.TeacherJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TeacherService {
    private final TeacherJpaRepository teacherJpaRepository;

}
