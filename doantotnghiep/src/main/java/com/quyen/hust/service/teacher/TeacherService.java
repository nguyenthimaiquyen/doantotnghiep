package com.quyen.hust.service.teacher;

import com.quyen.hust.model.response.teacher.TeacherResponse;
import com.quyen.hust.model.response.user.UserDataResponse;
import com.quyen.hust.repository.teacher.TeacherJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TeacherService {
    private final TeacherJpaRepository teacherJpaRepository;

    public List<TeacherResponse> getTeachers() {
        return teacherJpaRepository.findAll().stream().map(
                teacher -> TeacherResponse.builder()
                        .id(teacher.getId())
                        .user(UserDataResponse.builder()
                                .id(teacher.getUser().getId())
                                .fullName(teacher.getUser().getFullName())
                                .email(teacher.getUser().getEmail())
                                .build())
                        .build()
        ).collect(Collectors.toList());

    }






}
