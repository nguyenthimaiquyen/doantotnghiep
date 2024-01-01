package com.quyen.hust.repository;

import com.quyen.hust.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentJpaRepository extends JpaRepository<Enrollment, Long> {
}
