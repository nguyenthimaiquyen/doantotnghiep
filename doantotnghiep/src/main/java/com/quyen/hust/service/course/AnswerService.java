package com.quyen.hust.service.course;

import com.quyen.hust.entity.course.Answer;
import com.quyen.hust.model.request.course.AnswerRequest;
import com.quyen.hust.repository.course.AnswerJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AnswerService {
    private final AnswerJpaRepository answerJpaRepository;




}
