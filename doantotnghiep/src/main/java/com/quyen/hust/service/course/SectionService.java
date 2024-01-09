package com.quyen.hust.service.course;

import com.quyen.hust.repository.course.SectionJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SectionService {
    private final SectionJpaRepository sectionJpaRepository;



}
