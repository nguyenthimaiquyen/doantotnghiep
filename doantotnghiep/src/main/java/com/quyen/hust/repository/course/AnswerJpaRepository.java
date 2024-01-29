package com.quyen.hust.repository.course;

import com.quyen.hust.entity.course.Answer;
import com.quyen.hust.entity.course.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerJpaRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByQuizId(Long quizId);

    void deleteByQuizId(Long quizId);
}
