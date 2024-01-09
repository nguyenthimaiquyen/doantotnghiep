package com.quyen.hust.service.user;

import com.quyen.hust.model.request.user.StudyReminderRequest;
import com.quyen.hust.model.response.course.CourseDataResponse;
import com.quyen.hust.model.response.user.FrequencyResponse;
import com.quyen.hust.model.response.user.StudyReminderResponse;
import com.quyen.hust.repository.user.StudyReminderJpaRepository;
import com.quyen.hust.statics.Frequency;
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
        return List.of(
                FrequencyResponse.builder().code(Frequency.ONCE.getCode()).name(Frequency.ONCE.getName()).build(),
                FrequencyResponse.builder().code(Frequency.DAILY.getCode()).name(Frequency.DAILY.getName()).build(),
                FrequencyResponse.builder().code(Frequency.WEEKLY.getCode()).name(Frequency.WEEKLY.getName()).build()
        );
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
