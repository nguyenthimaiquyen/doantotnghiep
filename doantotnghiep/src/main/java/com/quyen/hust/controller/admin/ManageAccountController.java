package com.quyen.hust.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/accounts")
public class ManageAccountController {

    @GetMapping
    public String getAccountManagementPage(Model model) {
        return "admin/account/manage-account";
    }


}
