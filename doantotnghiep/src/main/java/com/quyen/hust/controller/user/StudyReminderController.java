package com.quyen.hust.controller.user;

import com.quyen.hust.exception.StudyReminderNotFoundException;
import com.quyen.hust.model.request.user.StudyReminderRequest;
import com.quyen.hust.service.course.CourseService;
import com.quyen.hust.service.user.StudyReminderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/v1/reminders")
@AllArgsConstructor
public class StudyReminderController {
    private final StudyReminderService studyReminderService;
    private final CourseService courseService;

    @GetMapping("/{userId}/courses")
    public ResponseEntity<?> getCourses(@PathVariable Long userId) {
        return ResponseEntity.ok(courseService.getEnrolledCourse(userId));
    }

    @GetMapping("/frequency")
    public ResponseEntity<?> getFrequency() {
        return ResponseEntity.ok(studyReminderService.getFrequency());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReminderDetails(@PathVariable Long id) throws StudyReminderNotFoundException {
        return ResponseEntity.ok(studyReminderService.getReminderDetails(id));
    }

    @PostMapping
    public ResponseEntity<?> createReminder(@RequestBody @Valid StudyReminderRequest request) {
        studyReminderService.saveReminder(request);
        return ResponseEntity.ok(null);
    }

    @PutMapping
    public ResponseEntity<?> updateReminder(@RequestBody @Valid StudyReminderRequest request) {
        studyReminderService.saveReminder(request);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReminder(@PathVariable Long id) {
        studyReminderService.deleteReminder(id);
        return ResponseEntity.ok(null);
    }

}
