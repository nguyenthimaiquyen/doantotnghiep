package com.quyen.hust.controller.course;

import com.quyen.hust.exception.CourseNotFoundException;
import com.quyen.hust.exception.UserNotFoundException;
import com.quyen.hust.model.request.course.CourseRequest;
import com.quyen.hust.model.request.course.CourseStatusRequest;
import com.quyen.hust.model.request.search.CourseSearchRequest;
import com.quyen.hust.model.response.course.CourseDataResponse;
import com.quyen.hust.model.response.course.CourseResponse;
import com.quyen.hust.service.course.CourseService;
import com.quyen.hust.service.teacher.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/courses")
public class CourseController {
    private final CourseService courseService;
    private final TeacherService teacherService;

    @GetMapping("/courseFeeUnit")
    public ResponseEntity<?> getCourseFeeUnit() {
        return ResponseEntity.ok(courseService.getCourseFeeUnit());
    }

    @GetMapping("/difficultyLevel")
    public ResponseEntity<?> getDifficultyLevel() {
        return ResponseEntity.ok(courseService.getDifficultyLevel());
    }

    @GetMapping("/trainingField")
    public ResponseEntity<?> getTrainingField() {
        return ResponseEntity.ok(courseService.getTrainingField());
    }

    @GetMapping("/discountCode")
    public ResponseEntity<?> getDiscountCode() {
        return ResponseEntity.ok(courseService.getDiscountCode());
    }

    @GetMapping("/teachers")
    public ResponseEntity<?> getTeachers() {
        return ResponseEntity.ok(teacherService.getTeachers());
    }

    @GetMapping
    public ResponseEntity<?> getCourseTable(CourseSearchRequest request) {
        CourseResponse courses = courseService.searchCourse(request);
        return ResponseEntity.ok(courses);
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody @Valid CourseRequest request) {
        courseService.saveCourse(request);
        return ResponseEntity.ok(null);
    }

    @PutMapping
    public ResponseEntity<?> updateCourse(@RequestBody @Valid CourseRequest request) {
        courseService.saveCourse(request);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseDetails(@PathVariable Long id) throws CourseNotFoundException {
        return ResponseEntity.ok(courseService.getCourseDetails(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changeCourseStatus(@PathVariable Long id, @RequestBody @Valid CourseStatusRequest courseStatus)
            throws CourseNotFoundException {
        courseService.changeCourseStatus(id, courseStatus);
        return ResponseEntity.ok(null);
    }

}
