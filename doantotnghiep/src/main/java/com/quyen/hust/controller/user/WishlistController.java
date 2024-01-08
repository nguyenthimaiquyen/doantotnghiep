package com.quyen.hust.controller.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/wishlist")
public class WishlistController {

    @GetMapping
    public String getCartPage(Model model) {
        return "wishlist/wishlist";
    }
}
