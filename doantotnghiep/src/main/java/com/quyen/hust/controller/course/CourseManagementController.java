package com.quyen.hust.controller.course;

import com.quyen.hust.service.course.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/courses/management")
public class CourseManagementController {
    private final CourseService courseService;

    @GetMapping
    public String getCourseManagementPage(Model model) {
        return "course/course-management";
    }

    @GetMapping("creation")
    public String getCourseCreationPage(Model model) {
        return "course/course-creation";
    }

    @GetMapping("section")
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



}
