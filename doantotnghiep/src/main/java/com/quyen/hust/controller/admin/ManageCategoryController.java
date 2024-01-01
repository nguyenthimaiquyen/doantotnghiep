package com.quyen.hust.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/categories")
public class ManageCategoryController {

    @GetMapping
    public String getCategoryManagementPage(Model model) {
        return "admin/category/manage-category";
    }



}
