package com.quyen.hust.controller.user;

import com.quyen.hust.exception.CourseNotFoundException;
import com.quyen.hust.model.request.user.CartItemRequest;
import com.quyen.hust.model.request.user.WishlistItemRequest;
import com.quyen.hust.service.user.CartItemService;
import com.quyen.hust.service.user.UserService;
import com.quyen.hust.service.user.WishlistItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/wishlistItems")
public class WishlistItemController {
    private final WishlistItemService wishlistItemService;

    @PostMapping
    public ResponseEntity<?> createWishlistItem(@RequestBody @Valid WishlistItemRequest request) throws CourseNotFoundException {
        wishlistItemService.createWishlistItem(request);
        return ResponseEntity.ok(request.getUserId());
    }

    @DeleteMapping("/{wishlistItemId}")
    public ResponseEntity<?> deleteWishlistItem(@PathVariable Long wishlistItemId) {
        wishlistItemService.deleteWishlistItem(wishlistItemId);
        return ResponseEntity.ok(null);
    }

}
