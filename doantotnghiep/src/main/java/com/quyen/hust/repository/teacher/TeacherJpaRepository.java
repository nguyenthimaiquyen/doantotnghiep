package com.quyen.hust.repository.teacher;

import com.quyen.hust.entity.teacher.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherJpaRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByUserEmail(String email);

    Teacher findByUserId(Long userId);

}
