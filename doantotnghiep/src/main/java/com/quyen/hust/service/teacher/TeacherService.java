package com.quyen.hust.service.teacher;

import com.quyen.hust.model.response.teacher.TeacherResponse;
import com.quyen.hust.repository.teacher.TeacherJpaRepository;
import com.quyen.hust.repository.user.UserJpaRepository;
import com.quyen.hust.statics.Roles;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TeacherService {
    private final TeacherJpaRepository teacherJpaRepository;
    private final UserJpaRepository userJpaRepository;

    public List<TeacherResponse> getTeachers() {
        return userJpaRepository.findByRolesNameContaining(Roles.TEACHER.toString()).stream().map(
                user -> TeacherResponse.builder()
                        .user(user)
                        .build()
        ).collect(Collectors.toList());

    }


}
