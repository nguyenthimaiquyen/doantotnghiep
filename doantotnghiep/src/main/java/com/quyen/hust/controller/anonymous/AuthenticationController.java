package com.quyen.hust.controller.anonymous;

import com.quyen.hust.entity.RefreshToken;
import com.quyen.hust.entity.user.User;
import com.quyen.hust.exception.RefreshTokenNotFoundException;
import com.quyen.hust.model.request.RefreshTokenRequest;
import com.quyen.hust.model.request.anonymous.LoginRequest;
import com.quyen.hust.model.request.anonymous.RegistrationRequest;
import com.quyen.hust.model.response.JwtResponse;
import com.quyen.hust.repository.RefreshTokenRepository;
import com.quyen.hust.repository.user.UserJpaRepository;
import com.quyen.hust.security.CustomUserDetails;
import com.quyen.hust.security.JwtUtils;
import com.quyen.hust.service.user.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController {
    private final JwtUtils jwtUtils;

    private final UserService userService;

    private final UserJpaRepository userJpaRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String refreshToken = UUID.randomUUID().toString();
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .refreshToken(refreshToken)
                .user(userJpaRepository.findById(userDetails.getId()).get())
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        User user = userJpaRepository.findById(userDetails.getId()).get();

        return JwtResponse.builder()
                .jwt(jwt)
                .refreshToken(refreshToken)
                .id(userDetails.getId())
                .email(userDetails.getUsername())
                .fullName(user.getFullName())
                .avatar(user.getAvatar())
                .roles(roles)
                .build();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest request) {
        User user = userJpaRepository.findByEmail(request.getEmail());
        if (!ObjectUtils.isEmpty(user)) {
            return new ResponseEntity<>("Username is existed", HttpStatus.BAD_REQUEST);
        } else {
            userService.registerUser(request);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        try {
            return ResponseEntity.ok(userService.refreshToken(request));
        } catch (RefreshTokenNotFoundException | UsernameNotFoundException ex) {
            return new ResponseEntity<>("Thông tin refreshToken không chính xác", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        userService.logout();
        return ResponseEntity.ok(null);
    }



}
