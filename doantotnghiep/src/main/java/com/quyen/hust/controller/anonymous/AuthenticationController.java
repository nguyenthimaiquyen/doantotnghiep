package com.quyen.hust.controller.anonymous;

import com.quyen.hust.entity.RefreshToken;
import com.quyen.hust.entity.user.User;
import com.quyen.hust.exception.RefreshTokenNotFoundException;
import com.quyen.hust.exception.UnauthorizedException;
import com.quyen.hust.model.request.RefreshTokenRequest;
import com.quyen.hust.model.request.anonymous.LoginRequest;
import com.quyen.hust.model.request.anonymous.RegistrationRequest;
import com.quyen.hust.model.response.JwtResponse;
import com.quyen.hust.repository.RefreshTokenRepository;
import com.quyen.hust.repository.user.UserJpaRepository;
import com.quyen.hust.security.CustomUserDetails;
import com.quyen.hust.security.JwtUtils;
import com.quyen.hust.service.admin.EmailService;
import com.quyen.hust.service.user.UserService;
import com.quyen.hust.statics.UserStatus;
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
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
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
    private final EmailService emailService;

    @PostMapping("/login")
    public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest request) throws UnauthorizedException {
        //thực hiện quá trình xác thực qua username và password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        //cập nhật thông tin xác thực trong SecurityContextHolder để có thể truy cập từ bất kỳ nơi nào trong ứng dụng
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //lấy thông tin chi tiết về người dùng sau khi xác thực
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        //lấy thông tin người dùng từ CSDL để thêm vào đối tượng JwtResponse
        User user = userJpaRepository.findById(userDetails.getId()).get();
        //kiểm tra trạng thái của user
        if (user != null && !user.getUserStatus().equals(UserStatus.ACTIVATED)) {
            throw new UnauthorizedException("Tài khoản chưa được kích hoạt");
        }
        //sử dụng jwtUtils để tạo ra JWT Token dựa trên thông tin xác thực
        String jwt = jwtUtils.generateJwtToken(authentication);
        //lấy danh sách các quyền của người dùng từ thông tin chi tiết người dùng
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        //tạo 1 refresh token mới sử dụng UUID
        String refreshToken = UUID.randomUUID().toString();
        //tạo 1 đối tượng refresh token để lưu thông tin về refresh token và người dùng liên quan
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .refreshToken(refreshToken)
                .user(userJpaRepository.findById(userDetails.getId()).get())
                .build();
        //lưu refresh token vào CSDL
        refreshTokenRepository.save(refreshTokenEntity);
        //trả về đối tượng JwtResponse
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
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest request) throws MessagingException {
        User user = userJpaRepository.findByEmail(request.getEmail());
        if (!ObjectUtils.isEmpty(user)) {
            return new ResponseEntity<>("Username is existed", HttpStatus.BAD_REQUEST);
        } else {
            User newUser = userService.registerUser(request);
            emailService.verifyAccount(newUser.getId(), request.getFullName(), request.getEmail(), request.getRole());
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
