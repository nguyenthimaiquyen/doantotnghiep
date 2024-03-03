package com.quyen.hust.repository.user;

import com.quyen.hust.entity.user.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentJpaRepository extends JpaRepository<Enrollment, Long> {

    Optional<Enrollment> findByCourseIdAndUserId(Long courseId, Long userId);

    List<Enrollment> findByUserId(Long userId);

}
