package com.quyen.hust.service;

import com.quyen.hust.repository.LessonJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LessonService {
    private final LessonJpaRepository lessonJpaRepository;



}
