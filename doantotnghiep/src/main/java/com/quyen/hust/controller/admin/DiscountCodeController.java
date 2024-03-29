package com.quyen.hust.controller.admin;

import com.quyen.hust.exception.DiscountCodeNotFoundException;
import com.quyen.hust.model.request.admin.DiscountCodeRequest;
import com.quyen.hust.service.admin.DiscountCodeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/discount-codes")
public class DiscountCodeController {
    private final DiscountCodeService discountCodeService;

    @GetMapping("/unit")
    public ResponseEntity<?> getDiscountCodeUnit() {
        return ResponseEntity.ok(discountCodeService.getDiscountCodeUnit());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDiscountCodeDetails(@PathVariable Long id) throws DiscountCodeNotFoundException {
        return ResponseEntity.ok(discountCodeService.getDiscountCodeDetails(id));
    }

    @PostMapping
    public ResponseEntity<?> createDiscountCode(@RequestBody @Valid DiscountCodeRequest request) {
        discountCodeService.saveDiscountCode(request);
        return ResponseEntity.ok(null);
    }

    @PutMapping
    public ResponseEntity<?> updateDiscountCode(@RequestBody @Valid DiscountCodeRequest request) {
        discountCodeService.saveDiscountCode(request);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiscountCode(@PathVariable Long id) {
        discountCodeService.deleteDiscountCode(id);
        return ResponseEntity.ok(null);
    }

}
