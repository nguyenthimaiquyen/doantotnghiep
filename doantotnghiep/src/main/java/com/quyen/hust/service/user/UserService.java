package com.quyen.hust.service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quyen.hust.entity.admin.Role;
import com.quyen.hust.entity.user.User;
import com.quyen.hust.exception.ExistedUserException;
import com.quyen.hust.exception.PasswordNotMatchedException;
import com.quyen.hust.exception.RefreshTokenNotFoundException;
import com.quyen.hust.exception.UserNotFoundException;
import com.quyen.hust.model.request.RefreshTokenRequest;
import com.quyen.hust.model.request.anonymous.CreateUserRequest;
import com.quyen.hust.model.request.anonymous.RegistrationRequest;
import com.quyen.hust.model.request.user.PasswordRequest;
import com.quyen.hust.model.request.user.UserSearchRequest;
import com.quyen.hust.model.response.CommonResponse;
import com.quyen.hust.model.response.JwtResponse;
import com.quyen.hust.model.response.user.UserResponse;
import com.quyen.hust.model.response.user.UserSearchResponse;
import com.quyen.hust.repository.RefreshTokenRepository;
import com.quyen.hust.repository.admin.RoleJpaRepository;
import com.quyen.hust.repository.user.UserCustomRepository;
import com.quyen.hust.repository.user.UserJpaRepository;
import com.quyen.hust.security.CustomUserDetails;
import com.quyen.hust.security.JwtUtils;
import com.quyen.hust.security.SecurityUtils;
import com.quyen.hust.statics.Roles;
import com.quyen.hust.statics.UserStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserJpaRepository userJpaRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleJpaRepository roleJpaRepository;

    private final ObjectMapper objectMapper;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserCustomRepository userCustomRepository;

    private final JwtUtils jwtUtils;

    @Value("${application.security.refreshToken.tokenValidityMilliseconds}")
    private long refreshTokenValidityMilliseconds;

    public UserService(PasswordEncoder passwordEncoder, UserJpaRepository userJpaRepository,
                       RoleJpaRepository roleJpaRepository, ObjectMapper objectMapper,
                       RefreshTokenRepository refreshTokenRepository, UserCustomRepository userCustomRepository, JwtUtils jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.userJpaRepository = userJpaRepository;
        this.roleJpaRepository = roleJpaRepository;
        this.objectMapper = objectMapper;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userCustomRepository = userCustomRepository;
        this.jwtUtils = jwtUtils;
    }

    public void registerUser(RegistrationRequest registrationRequest) {
        Optional<Role> optionalRole = roleJpaRepository.findByName(Roles.USER);
        Set<Role> roles = new HashSet<>();
        roles.add(optionalRole.get());
        User user = User.builder()
                .fullName(registrationRequest.getFullName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .roles(roles)
                .userStatus(UserStatus.CREATED)
                .build();
        userJpaRepository.save(user);
    }


    public List<UserResponse> getAll() {
        List<User> users = userJpaRepository.findAll();
        if (!CollectionUtils.isEmpty(users)) {
            return users.stream().map(u -> objectMapper.convertValue(u, UserResponse.class)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public UserResponse getDetail(Long id) throws ClassNotFoundException {
        return userJpaRepository.findById(id).map(u -> objectMapper.convertValue(u, UserResponse.class)).orElseThrow(ClassNotFoundException::new);
    }

    public JwtResponse refreshToken(RefreshTokenRequest request) throws RefreshTokenNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String newToken = userJpaRepository.findById(userDetails.getId())
                .flatMap(user -> refreshTokenRepository.findByUserAndRefreshTokenAndInvalidated(user, request.getRefreshToken(), false)
                        .map(refreshToken -> {
                            LocalDateTime createdDateTime = refreshToken.getCreatedDateTime();
                            LocalDateTime expiryTime = createdDateTime.plusSeconds(refreshTokenValidityMilliseconds / 1000);
                            if (expiryTime.isBefore(LocalDateTime.now())) {
                                refreshToken.setInvalidated(true);
                                refreshTokenRepository.save(refreshToken);
                                return null;
                            }
                            return jwtUtils.generateJwtToken(authentication);
                        }))
                .orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại"));

        if (newToken == null) {
            throw new RefreshTokenNotFoundException();
        }
        return JwtResponse.builder()
                .jwt(newToken)
                .build();
    }

    @Transactional
    public void logout() {
        Optional<Long> userIdOptional = SecurityUtils.getCurrentUserLoginId();
        if (userIdOptional.isEmpty()) {
            throw new UsernameNotFoundException("Tài khoản không tồn tại");
        }
        refreshTokenRepository.logOut(userIdOptional.get());
        SecurityContextHolder.clearContext();
    }

    public void createUser(CreateUserRequest request) throws ExistedUserException {
        User UserNeedCheck = userJpaRepository.findByEmail(request.getEmail());
        if (ObjectUtils.isEmpty(UserNeedCheck)) {
            throw new ExistedUserException();
        }

        Set<Role> roles = roleJpaRepository.findByName(Roles.USER).stream().collect(Collectors.toSet());

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode("123"))
                .roles(roles)
                .build();
        userJpaRepository.save(user);
    }

    public CommonResponse<?> searchUser(UserSearchRequest request) {
        List<UserSearchResponse> users = userCustomRepository.searchUser(request);

        Integer pageIndex = request.getPageIndex();
        Integer pageSize = request.getPageSize();
        double pageNumber = Math.ceil((float) users.size() / pageSize);

        users = users.subList((pageIndex - 1) * pageSize + 1, pageIndex * pageSize + 1);

        return CommonResponse.builder()
                .pageNumber((int) pageNumber)
                .data(users)
                .build();
    }

    public void changePassword(PasswordRequest request) throws UserNotFoundException, PasswordNotMatchedException {
        User user = userJpaRepository.findByEmail(request.getEmail());
        if (ObjectUtils.isEmpty(user)) {
            throw new UserNotFoundException("User could not be found");
        }
        if (!request.getNewPassword().equals(request.getRenewPassword())) {
            throw new PasswordNotMatchedException("Password don't matched");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userJpaRepository.save(user);
    }




}
