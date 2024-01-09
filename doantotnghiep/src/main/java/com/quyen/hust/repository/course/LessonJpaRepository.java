package com.quyen.hust.repository.course;

import com.quyen.hust.entity.course.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonJpaRepository extends JpaRepository<Lesson, Long> {

}
