package com.quyen.hust.controller.user;

import com.quyen.hust.exception.CourseNotFoundException;
import com.quyen.hust.model.request.user.CartItemRequest;
import com.quyen.hust.service.user.CartItemService;
import com.quyen.hust.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/cartItems")
public class CartItemController {
    private final CartItemService cartItemService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createCartItem(@RequestBody @Valid CartItemRequest request) throws CourseNotFoundException {
        cartItemService.createCartItem(request);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long cartItemId) {
        cartItemService.deleteCartItem(cartItemId);
        return ResponseEntity.ok(null);
    }


}
