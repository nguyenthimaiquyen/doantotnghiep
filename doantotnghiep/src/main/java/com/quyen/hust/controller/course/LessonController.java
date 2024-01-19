package com.quyen.hust.controller.course;

import com.google.gson.Gson;
import com.quyen.hust.exception.LessonNotFoundException;
import com.quyen.hust.model.request.course.LessonRequest;
import com.quyen.hust.service.course.LessonService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/lessons")
public class LessonController {
    private final LessonService lessonService;
    private final Gson gson;

    @GetMapping("/{id}")
    public ResponseEntity<?> getLessonDetails(@PathVariable Long id) throws LessonNotFoundException {
        return ResponseEntity.ok(lessonService.getLessonDetails(id));
    }

    @GetMapping
    public ResponseEntity<?> getLessons(@PathVariable Long sectionId) {
        return ResponseEntity.ok(lessonService.getLessons(sectionId));
    }

    @PostMapping
    public ResponseEntity<?> createLesson(@RequestPart("lessonRequest") String lessonRequest) {
        LessonRequest request = gson.fromJson(lessonRequest, LessonRequest.class);
        lessonService.saveLesson(request);
        return ResponseEntity.ok(null);
    }

    @PutMapping
    public ResponseEntity<?> updateLesson(@RequestBody @Valid LessonRequest request) {
        lessonService.saveLesson(request);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.ok(null);
    }


}
