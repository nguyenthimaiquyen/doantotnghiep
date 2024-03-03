package com.quyen.hust.controller.course;

import com.quyen.hust.exception.AnswerNotFoundException;
import com.quyen.hust.exception.QuizNotFoundException;
import com.quyen.hust.exception.SectionNotFoundException;
import com.quyen.hust.model.request.course.AnswerRequest;
import com.quyen.hust.model.request.course.QuizRequest;
import com.quyen.hust.service.course.QuizService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/quizzes")
public class QuizController {
    private final QuizService quizService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizDetails(@PathVariable Long id) throws QuizNotFoundException {
        return ResponseEntity.ok(quizService.getQuizDetails(id));
    }

    @PostMapping
    public ResponseEntity<?> createQuiz(@RequestBody @Valid QuizRequest request) throws SectionNotFoundException, AnswerNotFoundException {
        quizService.saveQuiz(request);
        return ResponseEntity.ok(null);
    }

    @PutMapping
    public ResponseEntity<?> updateQuiz(@RequestBody @Valid QuizRequest request) throws SectionNotFoundException, AnswerNotFoundException {
        quizService.saveQuiz(request);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable Long id) throws QuizNotFoundException {
        quizService.deleteQuiz(id);
        return ResponseEntity.ok(null);
    }



}
