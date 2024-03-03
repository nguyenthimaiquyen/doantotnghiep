package com.quyen.hust.repository.user;

import com.quyen.hust.entity.user.StudyReminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyReminderJpaRepository extends JpaRepository<StudyReminder, Long> {

    List<StudyReminder> findByUserId(Long userId);
}
