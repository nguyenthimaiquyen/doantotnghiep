package com.quyen.hust.controller.user;

import com.quyen.hust.exception.ExistedUserException;
import com.quyen.hust.model.request.authentication.CreateUserRequest;
import com.quyen.hust.model.request.user.UserSearchRequest;
import com.quyen.hust.model.response.CommonResponse;
import com.quyen.hust.model.response.user.UserDataResponse;
import com.quyen.hust.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDataResponse getDetail(@PathVariable Long id) throws ClassNotFoundException {
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