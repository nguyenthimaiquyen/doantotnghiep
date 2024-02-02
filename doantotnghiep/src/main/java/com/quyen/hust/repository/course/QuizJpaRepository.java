package com.quyen.hust.repository.course;

import com.quyen.hust.entity.course.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizJpaRepository extends JpaRepository<Quiz, Long> {

    List<Quiz> findBySectionIdOrderByIdAsc(Long sectionId);

    List<Quiz> findBySectionId(Long sectionId);


}
