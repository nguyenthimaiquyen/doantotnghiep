package com.quyen.hust.repository.teacher;

import com.quyen.hust.entity.teacher.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherJpaRepository extends JpaRepository<Teacher, Long> {
}
