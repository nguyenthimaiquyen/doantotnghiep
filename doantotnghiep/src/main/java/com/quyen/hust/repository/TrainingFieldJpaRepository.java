package com.quyen.hust.repository;

import com.quyen.hust.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingFieldJpaRepository extends JpaRepository<Teacher, Long> {
}
