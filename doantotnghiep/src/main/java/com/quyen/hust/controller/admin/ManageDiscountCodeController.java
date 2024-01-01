package com.quyen.hust.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/discountCode")
public class ManageDiscountCodeController {

    @GetMapping
    public String getDiscountCodeManagementPage(Model model) {
        return "admin/discount-code/manage-discount-code";
    }



}
