package com.quyen.hust.controller.course;

import com.google.gson.Gson;
import com.quyen.hust.exception.LessonNotFoundException;
import com.quyen.hust.exception.UnsupportedFormatException;
import com.quyen.hust.model.request.course.LessonRequest;
import com.quyen.hust.model.request.course.VideoDurationRequest;
import com.quyen.hust.service.course.LessonService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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

    @PostMapping
    public ResponseEntity<?> createLesson(@RequestPart("lessonRequest") String lessonRequest,
                                          @RequestPart(value = "file", required = false) MultipartFile file,
                                          @RequestPart(value = "video", required = false) MultipartFile video) throws UnsupportedFormatException {
        LessonRequest request = gson.fromJson(lessonRequest, LessonRequest.class);
        lessonService.saveLesson(request, file, video);
        return ResponseEntity.ok(null);
    }

    @PutMapping
    public ResponseEntity<?> updateLesson(@RequestPart("lessonRequest") String lessonRequest,
                                          @RequestPart(value = "file", required = false) MultipartFile file,
                                          @RequestPart(value = "video", required = false) MultipartFile video) throws UnsupportedFormatException {
        LessonRequest request = gson.fromJson(lessonRequest, LessonRequest.class);
        lessonService.saveLesson(request, file, video);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/video-duration")
    public ResponseEntity<?> updateVideoDuration(@RequestBody VideoDurationRequest videoDurationRequest) throws LessonNotFoundException {
        lessonService.saveVideoDuration(videoDurationRequest);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable Long id) throws LessonNotFoundException {
        lessonService.deleteLesson(id);
        return ResponseEntity.ok(null);
    }


}
