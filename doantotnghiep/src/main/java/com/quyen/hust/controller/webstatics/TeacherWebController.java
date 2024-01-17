package com.quyen.hust.controller.webstatics;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class TeacherWebController {

    @GetMapping("/courses/analysis/teacher")
    public String getTeacherDashboardPage(Model model) {
        return "teacher/teacher-dashboard-management";
    }



}
