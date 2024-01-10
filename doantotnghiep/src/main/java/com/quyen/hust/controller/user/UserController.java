package com.quyen.hust.controller.user;

import com.quyen.hust.exception.ExistedUserException;
import com.quyen.hust.exception.PasswordNotMatchedException;
import com.quyen.hust.exception.UserNotFoundException;
import com.quyen.hust.model.request.anonymous.CreateUserRequest;
import com.quyen.hust.model.request.user.PasswordRequest;
import com.quyen.hust.model.request.user.UserSearchRequest;
import com.quyen.hust.model.response.CommonResponse;
import com.quyen.hust.model.response.user.UserResponse;
import com.quyen.hust.service.user.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
//
//    @GetMapping
//    public List<UserResponse> getAll() {
//        return userService.getAll();
//    }


    @GetMapping("/{id}")
    public UserResponse getDetail(@PathVariable Long id) throws ClassNotFoundException {
        return userService.getDetail(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CreateUserRequest request) {
        try {
            userService.createUser(request);
            return ResponseEntity.ok(null);
        } catch (ExistedUserException ex) {
            return new ResponseEntity<>("username đã tồn tại", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public CommonResponse<?> search(UserSearchRequest request) {
        return userService.searchUser(request);
    }

    @GetMapping("/v2")
    public ModelAndView search1(UserSearchRequest request) {
        ModelAndView modelAndView  = new ModelAndView("/users");
        modelAndView.addObject("userData", userService.searchUser(request));
        return modelAndView;
    }




}