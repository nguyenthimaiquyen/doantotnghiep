package com.quyen.hust.repository.user;

import com.quyen.hust.entity.user.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentJpaRepository extends JpaRepository<Enrollment, Long> {
}
