package com.quyen.hust.controller.course;

import com.quyen.hust.exception.AnswerNotFoundException;
import com.quyen.hust.exception.QuizNotFoundException;
import com.quyen.hust.exception.SectionNotFoundException;
import com.quyen.hust.model.request.course.AnswerRequest;
import com.quyen.hust.model.request.course.QuizRequest;
import com.quyen.hust.service.course.AnswerService;
import com.quyen.hust.service.course.QuizService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/answers")
public class AnswerController {
    private final QuizService quizService;
    private final AnswerService answerService;




}

