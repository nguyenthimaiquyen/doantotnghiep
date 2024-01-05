package com.quyen.hust.controller.teacher;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/courses/teacher")
public class TeacherCourseController {

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



}
