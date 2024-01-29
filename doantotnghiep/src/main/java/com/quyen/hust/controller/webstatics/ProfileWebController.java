package com.quyen.hust.controller.webstatics;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/profile")
public class ProfileWebController {

    @GetMapping
    public String getProfilePage(Model model) {
        return "profile/profile";
    }

    @GetMapping("/otp-sending")
    public String getOtpSendingPage(Model model) {
        return "profile/otp-sending";
    }

    @GetMapping("/forget-password")
    public String getForgetPasswordPage(Model model) {
        return "profile/forget-password";
    }


}
