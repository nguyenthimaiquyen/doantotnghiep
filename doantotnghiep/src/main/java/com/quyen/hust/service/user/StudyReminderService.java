package com.quyen.hust.service.user;

import com.quyen.hust.repository.user.StudyReminderJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudyReminderService {
    private final StudyReminderJpaRepository studyReminderJpaRepository;



}
