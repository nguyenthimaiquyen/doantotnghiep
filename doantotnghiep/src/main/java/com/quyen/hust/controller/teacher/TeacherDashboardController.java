package com.quyen.hust.controller.teacher;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/courses/analysis/teacher")
public class TeacherDashboardController {

    @GetMapping
    public String getDashboardPage(Model model) {
        return "teacher/teacher-dashboard-management";
    }



}
