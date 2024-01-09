package com.quyen.hust.repository.admin;

import com.quyen.hust.entity.admin.TrainingField;
import com.quyen.hust.entity.teacher.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingFieldJpaRepository extends JpaRepository<TrainingField, Long> {
}
