package com.quyen.hust.controller.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/payments")
public class CheckoutController {

    @GetMapping
    public String getHomePage(Model model) {
        return "cart/checkout";
    }
}
