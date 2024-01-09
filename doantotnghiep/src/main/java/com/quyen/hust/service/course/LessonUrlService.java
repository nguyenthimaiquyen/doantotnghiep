package com.quyen.hust.service.course;

import com.quyen.hust.repository.course.LessonUrlJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LessonUrlService {
    private final LessonUrlJpaRepository lessonUrlJpaRepository;

}
