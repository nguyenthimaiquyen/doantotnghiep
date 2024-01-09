package com.quyen.hust.controller.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/profiles")
public class ProfileController {

    @GetMapping
    public String getProfilePage(Model model) {
        return "profile/profile-user";
    }

}
