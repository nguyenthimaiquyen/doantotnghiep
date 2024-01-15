package com.quyen.hust.controller.course;

import com.quyen.hust.model.request.course.CourseRequest;
import com.quyen.hust.model.response.teacher.TeacherResponse;
import com.quyen.hust.service.course.CourseService;
import com.quyen.hust.service.teacher.TeacherService;
import com.quyen.hust.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;
    private final TeacherService teacherService;

    @GetMapping("/management")
    public String getCourseManagementPage(Model model) {
        return "course/course-management";
    }

    @GetMapping("/creation")
    public String getCourseCreationPage(Model model) {
        return "course/course-creation";
    }

    @GetMapping("/section")
    public String getSectionCreationPage(Model model) {
        return "course/section-creation";
    }

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

    @DeleteMapping
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(null);
    }



}
