package com.quyen.hust.controller.admin;

import com.quyen.hust.exception.UserNotFoundException;
import com.quyen.hust.model.request.admin.AccountStatusRequest;
import com.quyen.hust.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final UserService userService;

    @PutMapping("/{id}")
    public ResponseEntity<?> changeAccountStatus(@PathVariable Long id, @RequestBody @Valid AccountStatusRequest accountStatus)
            throws UserNotFoundException {
        userService.changeAccountStatus(id, accountStatus);
        return ResponseEntity.ok(null);
    }



}
