package com.quyen.hust.repository.course;

import com.quyen.hust.entity.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseJpaRepository extends JpaRepository<Course, Long> {
}
