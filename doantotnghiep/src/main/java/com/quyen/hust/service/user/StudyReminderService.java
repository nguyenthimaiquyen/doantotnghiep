package com.quyen.hust.service.user;

import com.quyen.hust.model.request.user.StudyReminderRequest;
import com.quyen.hust.model.response.CourseDataResponse;
import com.quyen.hust.model.response.user.FrequencyResponse;
import com.quyen.hust.model.response.user.StudyReminderResponse;
import com.quyen.hust.repository.user.StudyReminderJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudyReminderService {
    private final StudyReminderJpaRepository studyReminderJpaRepository;


    public List<CourseDataResponse> getCourses() {
        return null;
    }

    public List<FrequencyResponse> getFrequency() {
        return null;
    }

    public List<StudyReminderResponse> getAll() {
        return null;
    }

    public StudyReminderResponse getReminderDetails(Long id) {
        return null;
    }

    public void saveReminder(StudyReminderRequest request) {

    }

    public void deleteReminder(Long id) {

    }
}
