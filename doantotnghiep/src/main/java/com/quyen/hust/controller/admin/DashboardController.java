package com.quyen.hust.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/courses/analysis/admin")
public class DashboardController {

    @GetMapping
    public String getDashboardPage(Model model) {
        return "admin/dashboard/admin-dashboard-management";
    }


}
