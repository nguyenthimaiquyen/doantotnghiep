package com.quyen.hust.controller.user;


import com.quyen.hust.exception.PasswordNotMatchedException;
import com.quyen.hust.exception.UserNotFoundException;
import com.quyen.hust.model.request.user.PasswordRequest;
import com.quyen.hust.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/profiles")
public class ProfileController {
    private final UserService userService;

    @GetMapping
    public String getProfilePage(Model model) {
        return "profile/profile-user";
    }

    @GetMapping("/forgetPassword")
    public String getForgetPasswordPage(Model model) {
        return "profile/forgetPassword";
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody @Valid PasswordRequest request)
            throws UserNotFoundException, PasswordNotMatchedException {
        userService.changePassword(request);
        return ResponseEntity.ok(null);
    }



}
