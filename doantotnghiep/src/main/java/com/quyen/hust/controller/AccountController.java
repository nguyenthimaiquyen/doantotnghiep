package com.quyen.hust.controller;

import com.quyen.hust.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final UserService userService;

    @GetMapping
    public String getAccountManagementPage(Model model) {
        return "admin/account/manage-account";
    }

    @GetMapping("/{id}/activation")
    public String getAccountActivationPage(@PathVariable Long id) {
        userService.verifyAccount(id);
        return "layout/account-activation";
    }


}
