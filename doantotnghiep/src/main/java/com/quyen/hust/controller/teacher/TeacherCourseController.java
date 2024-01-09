package com.quyen.hust.controller.teacher;

import com.quyen.hust.service.course.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/courses/teacher")
public class TeacherCourseController {
    private final CourseService courseService;

    @GetMapping
    public String getCourseManagementPage(Model model) {
        return "teacher/course/teacher-course-management";
    }

    @GetMapping("creation")
    public String getCourseCreationPage(Model model) {
        return "teacher/course/teacher-course-creation";
    }

    @GetMapping("section")
    public String getSectionCreationPage(Model model) {
        return "teacher/course/teacher-section-creation";
    }

    @GetMapping("/courseFeeUnit")
    public ResponseEntity<?> getCourseFeeUnit() {
        return ResponseEntity.ok(courseService.getCourseFeeUnit());
    }

    @GetMapping("/difficultyLevel")
    public ResponseEntity<?> getDifficultyLevel() {
        return ResponseEntity.ok(courseService.getDifficultyLevel());
    }



}
