package com.quyen.hust.service.teacher;

import com.quyen.hust.entity.admin.Role;
import com.quyen.hust.entity.teacher.Teacher;
import com.quyen.hust.entity.user.User;
import com.quyen.hust.model.request.anonymous.RegistrationRequest;
import com.quyen.hust.model.response.teacher.TeacherResponse;
import com.quyen.hust.repository.admin.RoleJpaRepository;
import com.quyen.hust.repository.teacher.TeacherJpaRepository;
import com.quyen.hust.repository.user.UserJpaRepository;
import com.quyen.hust.statics.Roles;
import com.quyen.hust.statics.UserStatus;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TeacherService {
    private final TeacherJpaRepository teacherJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleJpaRepository roleJpaRepository;

    public List<TeacherResponse> getTeachers() {
        return teacherJpaRepository.findAll().stream().map(
                teacher -> TeacherResponse.builder()
                        .id(teacher.getId())
                        .user(teacher.getUser())
                        .build()
        ).collect(Collectors.toList());

    }


    public Teacher registerTeacher(RegistrationRequest registrationRequest) {
        //đăng ký với role là teacher
        Set<Role> roles = new HashSet<>();
        Optional<Role> optionalRole = roleJpaRepository.findByName(Roles.TEACHER);
        roles.add(optionalRole.get());
        User user = User.builder()
                .fullName(registrationRequest.getFullName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .roles(roles)
                .userStatus(UserStatus.CREATED)
                .build();
        Teacher teacher = Teacher.builder()
                .user(user)
                .build();
        teacherJpaRepository.save(teacher);
        return teacher;
    }



}
