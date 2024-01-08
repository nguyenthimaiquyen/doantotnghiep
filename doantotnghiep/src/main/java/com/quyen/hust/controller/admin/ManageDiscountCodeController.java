package com.quyen.hust.controller.admin;

import com.quyen.hust.exception.DiscountCodeNotFoundException;
import com.quyen.hust.model.request.admin.DiscountCodeRequest;
import com.quyen.hust.model.response.admin.DiscountCodeResponse;
import com.quyen.hust.service.admin.DiscountCodeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/discountCodes")
public class ManageDiscountCodeController {
    private final DiscountCodeService discountCodeService;

    @GetMapping
    public String getDiscountCodeManagementPage(Model model) {
        List<DiscountCodeResponse> discountCodes = discountCodeService.getAll();
        model.addAttribute("discountCodes", discountCodes);
        return "admin/discount-code/manage-discount-code";
    }

    @GetMapping("/unit")
    public ResponseEntity<?> getDiscountCodeUnit() {
        return ResponseEntity.ok(discountCodeService.getDiscountCodeUnit());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDiscountCodeDetails(@PathVariable Long id) throws DiscountCodeNotFoundException {
        return ResponseEntity.ok(discountCodeService.getDiscountCodeDetails(id));
    }

    @PostMapping
    public ResponseEntity<?> createDiscountCode(@RequestBody DiscountCodeRequest request) {
        discountCodeService.saveDiscountCode(request);
        return ResponseEntity.ok(null);
    }

    @PutMapping
    public ResponseEntity<?> updateDiscountCode(@RequestBody DiscountCodeRequest request) {
        discountCodeService.saveDiscountCode(request);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiscountCode(@PathVariable Long id) {
        discountCodeService.deleteDiscountCode(id);
        return ResponseEntity.ok(null);
    }

}
