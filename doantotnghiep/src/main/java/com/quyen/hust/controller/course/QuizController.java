//package com.quyen.hust.controller.course;
//
//import com.google.gson.Gson;
//import com.quyen.hust.exception.UnsupportedFormatException;
//import com.quyen.hust.model.request.course.QuizRequest;
//import lombok.AllArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//
//@Controller
//@AllArgsConstructor
//@RequestMapping("/api/v1/quizzes")
//public class QuizController {
//    private final QuizService quizService;
//    private final Gson gson;
//
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getQuizDetails(@PathVariable Long id) throws QuizNotFoundException {
//        return ResponseEntity.ok(quizService.getQuizDetails(id));
//    }
//
//    @GetMapping
//    public ResponseEntity<?> getQuizs(@PathVariable Long sectionId) {
//        return ResponseEntity.ok(quizService.getQuizs(sectionId));
//    }
//
//    @PostMapping
//    public ResponseEntity<?> createQuiz(@RequestPart("quizRequest") String quizRequest,
//                                          @RequestPart(value = "file", required = false) MultipartFile file,
//                                          @RequestPart(value = "video", required = false) MultipartFile video) throws UnsupportedFormatException {
//        QuizRequest request = gson.fromJson(quizRequest, QuizRequest.class);
//        quizService.saveQuiz(request, file, video);
//        return ResponseEntity.ok(null);
//    }
//
//    @PutMapping
//    public ResponseEntity<?> updateQuiz(@RequestPart("quizRequest") String quizRequest,
//                                          @RequestPart(value = "file", required = false) MultipartFile file,
//                                          @RequestPart(value = "video", required = false) MultipartFile video) throws UnsupportedFormatException {
//        QuizRequest request = gson.fromJson(quizRequest, QuizRequest.class);
//        quizService.saveQuiz(request, file, video);
//        return ResponseEntity.ok(null);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteQuiz(@PathVariable Long id) {
//        quizService.deleteQuiz(id);
//        return ResponseEntity.ok(null);
//    }
//
//
//}
