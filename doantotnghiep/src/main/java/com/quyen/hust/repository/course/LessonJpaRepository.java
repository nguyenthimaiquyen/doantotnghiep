package com.quyen.hust.repository.course;

import com.quyen.hust.entity.course.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonJpaRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findBySectionIdOrderByIdAsc(Long sectionId);

    List<Lesson> findBySectionId(Long sectionId);
}
