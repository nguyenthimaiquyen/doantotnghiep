package com.quyen.hust.service;

import com.quyen.hust.repository.StudyReminderJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudyReminderService {
    private final StudyReminderJpaRepository studyReminderJpaRepository;



}
