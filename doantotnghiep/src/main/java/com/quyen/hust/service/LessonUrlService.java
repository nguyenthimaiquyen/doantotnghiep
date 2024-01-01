package com.quyen.hust.service;

import com.quyen.hust.repository.LessonUrlJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LessonUrlService {
    private final LessonUrlJpaRepository lessonUrlJpaRepository;

}
