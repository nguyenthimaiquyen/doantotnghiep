package com.quyen.hust.controller.user;

import com.quyen.hust.service.user.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartService cartService;

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> deleteAllCartItem(@PathVariable Long cartId) {
        cartService.deleteAllCartItem(cartId);
        return ResponseEntity.ok(null);
    }


}
