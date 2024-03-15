package com.quyen.hust.controller.user;

import com.quyen.hust.exception.CourseNotFoundException;
import com.quyen.hust.exception.EnrollmentNotFoundException;
import com.quyen.hust.exception.UserNotFoundException;
import com.quyen.hust.model.request.user.EnrollmentRequest;
import com.quyen.hust.model.response.user.EnrollmentResponse;
import com.quyen.hust.service.user.EnrollmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<?> createEnrollment(@RequestBody @Valid EnrollmentRequest request) throws CourseNotFoundException,
            UserNotFoundException {
        EnrollmentResponse enrollment = enrollmentService.createEnrollment(request);
        return ResponseEntity.ok(enrollment);
    }

    @GetMapping("/{userId}/{courseId}")
    public ResponseEntity<?> getEnrollment(@PathVariable Long courseId, @PathVariable Long userId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentByUserIdAndCourseId(userId, courseId));
    }

    @PutMapping
    public ResponseEntity<?> updateEnrollment(@RequestBody @Valid EnrollmentRequest request) throws EnrollmentNotFoundException {
        enrollmentService.updateEnrollment(request);
        return ResponseEntity.ok(null);
    }

}
