package com.quyen.hust.service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quyen.hust.entity.RefreshToken;
import com.quyen.hust.entity.admin.OTP;
import com.quyen.hust.entity.admin.Role;
import com.quyen.hust.entity.teacher.Teacher;
import com.quyen.hust.entity.user.Cart;
import com.quyen.hust.entity.user.User;
import com.quyen.hust.entity.user.Wishlist;
import com.quyen.hust.exception.*;
import com.quyen.hust.model.request.RefreshTokenRequest;
import com.quyen.hust.model.request.admin.AccountStatusRequest;
import com.quyen.hust.model.request.authentication.CreateUserRequest;
import com.quyen.hust.model.request.authentication.RegistrationRequest;
import com.quyen.hust.model.request.user.ChangePasswordRequest;
import com.quyen.hust.model.request.user.ForgetPasswordRequest;
import com.quyen.hust.model.request.user.UserSearchRequest;
import com.quyen.hust.model.response.CommonResponse;
import com.quyen.hust.model.response.JwtResponse;
import com.quyen.hust.model.response.user.UserDataResponse;
import com.quyen.hust.model.response.user.UserResponse;
import com.quyen.hust.repository.RefreshTokenRepository;
import com.quyen.hust.repository.admin.OTPJpaRepository;
import com.quyen.hust.repository.admin.RoleJpaRepository;
import com.quyen.hust.repository.teacher.TeacherJpaRepository;
import com.quyen.hust.repository.user.CartJpaRepository;
import com.quyen.hust.repository.user.UserCustomRepository;
import com.quyen.hust.repository.user.UserJpaRepository;
import com.quyen.hust.repository.user.WishlistJpaRepository;
import com.quyen.hust.security.CustomUserDetails;
import com.quyen.hust.security.JwtUtils;
import com.quyen.hust.security.SecurityUtils;
import com.quyen.hust.statics.Roles;
import com.quyen.hust.statics.UserStatus;
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
    private final OTPJpaRepository otpJpaRepository;
    private final TeacherJpaRepository teacherJpaRepository;
    private final CartJpaRepository cartJpaRepository;
    private final WishlistJpaRepository wishlistJpaRepository;


    @Value("${application.security.refreshToken.tokenValidityMilliseconds}")
    private long refreshTokenValidityMilliseconds;

    public UserService(PasswordEncoder passwordEncoder, UserJpaRepository userJpaRepository, RoleJpaRepository roleJpaRepository,
                       ObjectMapper objectMapper, RefreshTokenRepository refreshTokenRepository, TeacherJpaRepository teacherJpaRepository,
                       UserCustomRepository userCustomRepository, JwtUtils jwtUtils, OTPJpaRepository otpJpaRepository,
                       CartJpaRepository cartJpaRepository, WishlistJpaRepository wishlistJpaRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userJpaRepository = userJpaRepository;
        this.roleJpaRepository = roleJpaRepository;
        this.objectMapper = objectMapper;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userCustomRepository = userCustomRepository;
        this.jwtUtils = jwtUtils;
        this.otpJpaRepository = otpJpaRepository;
        this.teacherJpaRepository = teacherJpaRepository;
        this.wishlistJpaRepository = wishlistJpaRepository;
        this.cartJpaRepository = cartJpaRepository;
    }

    public User registerUser(RegistrationRequest registrationRequest) {
        Set<Role> roles = new HashSet<>();
        Optional<Role> optionalRole = roleJpaRepository.findByName(registrationRequest.getRole());
        roles.add(optionalRole.get());
        User user = User.builder()
                .fullName(registrationRequest.getFullName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .roles(roles)
                .userStatus(UserStatus.CREATED)
                .build();
        Cart cart = Cart.builder()
                .user(user)
                .build();
        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .build();
        if (registrationRequest.getRole().equals(Roles.TEACHER)) {
            Teacher teacher = Teacher.builder()
                    .user(user)
                    .build();
            teacherJpaRepository.save(teacher);
        }
        userJpaRepository.save(user);
        cartJpaRepository.save(cart);
        wishlistJpaRepository.save(wishlist);
        return user;
    }

    public List<UserDataResponse> getAll() {
        List<User> users = userJpaRepository.findAll();
        if (!CollectionUtils.isEmpty(users)) {
            return users.stream().map(u -> objectMapper.convertValue(u, UserDataResponse.class)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public UserDataResponse getDetail(Long id) throws ClassNotFoundException {
        return userJpaRepository.findById(id).map(u -> objectMapper.convertValue(u, UserDataResponse.class)).orElseThrow(ClassNotFoundException::new);
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
        String refreshToken = UUID.randomUUID().toString();
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .refreshToken(refreshToken)
                .user(userJpaRepository.findById(userDetails.getId()).get())
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        return JwtResponse.builder()
                .jwt(newToken)
                .refreshToken(refreshToken)
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
        List<UserResponse> users = userCustomRepository.searchUser(request);

        Integer pageIndex = request.getPageIndex();
        Integer pageSize = request.getPageSize();
        double pageNumber = Math.ceil((float) users.size() / pageSize);

        users = users.subList((pageIndex - 1) * pageSize + 1, pageIndex * pageSize + 1);

        return CommonResponse.builder()
                .pageNumber((int) pageNumber)
                .data(users)
                .build();
    }

    public void changePassword(ChangePasswordRequest request) throws UserNotFoundException, PasswordNotMatchedException {
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

    public String getUserName(String email) {
         return userJpaRepository.findByEmail(email).getFullName();
    }

    public void forgetPassword(ForgetPasswordRequest request) throws UserNotFoundException, OTPNotMatchedException,
            PasswordNotMatchedException, OTPNotFoundException, OTPExpiredException {
        OTP otp = otpJpaRepository.findByOtp(request.getOtp());
        if (ObjectUtils.isEmpty(otp)) {
            throw new OTPNotFoundException("OTP "+ request.getOtp() + " could not be found" );
        }
        if (!request.getNewPassword().equals(request.getRenewPassword())) {
            throw new PasswordNotMatchedException("Password don't matched");
        }
        //kiểm tra otp còn trong thời gian sống hay không
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime = otpJpaRepository.findByOtp(request.getOtp()).getLiveTime();
        if (currentTime.isAfter(expirationTime)) {
            throw new OTPExpiredException("OTP " + request.getOtp() + " has already expired");
        }
        User user = otp.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userJpaRepository.save(user);
    }

    public void verifyAccount(Long id) {
        Optional<User> user = userJpaRepository.findById(id);
        user.get().setUserStatus(UserStatus.ACTIVATED);
        userJpaRepository.save(user.get());
    }

    public void changeAccountStatus(Long id, AccountStatusRequest accountStatus) throws UserNotFoundException {
        User user = userJpaRepository.findById(id).get();
        if (ObjectUtils.isEmpty(user)) {
            throw new UserNotFoundException("User with id " + id + " could not be found!");
        }
        user.setUserStatus(accountStatus.getUserStatus());
        userJpaRepository.save(user);
    }


    public Long getUserId(String email) throws UserNotFoundException {
        User user = userJpaRepository.findByEmail(email);
        if (ObjectUtils.isEmpty(user)) {
            throw new UserNotFoundException("User with email " + email + " could not be found!");
        }
        return user.getId();
    }
}
