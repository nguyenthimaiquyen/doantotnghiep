package com.quyen.hust.service.course;

import com.quyen.hust.repository.course.LessonJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LessonService {
    private final LessonJpaRepository lessonJpaRepository;



}
