package com.quyen.hust.service.user;

import com.quyen.hust.entity.course.Course;
import com.quyen.hust.entity.user.StudyReminder;
import com.quyen.hust.entity.user.User;
import com.quyen.hust.exception.StudyReminderNotFoundException;
import com.quyen.hust.model.request.user.StudyReminderRequest;
import com.quyen.hust.model.response.course.CourseDataResponse;
import com.quyen.hust.model.response.user.FrequencyResponse;
import com.quyen.hust.model.response.user.StudyReminderResponse;
import com.quyen.hust.model.response.user.UserDataResponse;
import com.quyen.hust.repository.course.CourseJpaRepository;
import com.quyen.hust.repository.user.StudyReminderJpaRepository;
import com.quyen.hust.repository.user.UserJpaRepository;
import com.quyen.hust.statics.Frequency;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudyReminderService {
    private final StudyReminderJpaRepository studyReminderJpaRepository;
    private final CourseJpaRepository courseJpaRepository;
    private final UserJpaRepository userJpaRepository;

    public List<FrequencyResponse> getFrequency() {
        return List.of(
                FrequencyResponse.builder().code(Frequency.ONCE.getCode()).name(Frequency.ONCE.getName()).build(),
                FrequencyResponse.builder().code(Frequency.DAILY.getCode()).name(Frequency.DAILY.getName()).build(),
                FrequencyResponse.builder().code(Frequency.WEEKLY.getCode()).name(Frequency.WEEKLY.getName()).build()
        );
    }

    public StudyReminderResponse getReminderDetails(Long id) throws StudyReminderNotFoundException {
        return studyReminderJpaRepository.findById(id).map(
                studyReminder -> StudyReminderResponse.builder()
                        .id(studyReminder.getId())
                        .course(CourseDataResponse.builder()
                                .id(studyReminder.getCourse().getId())
                                .title(studyReminder.getCourse().getTitle())
                                .build())
                        .user(UserDataResponse.builder()
                                .id(studyReminder.getUser().getId())
                                .fullName(studyReminder.getUser().getFullName())
                                .build())
                        .endDate(studyReminder.getEndDate())
                        .startDate(studyReminder.getStartDate())
                        .eventName(studyReminder.getEventName())
                        .frequency(studyReminder.getFrequency())
                        .build()
        ).orElseThrow(() -> new StudyReminderNotFoundException("Reminder with id " + id + " could not be found"));
    }

    public void saveReminder(StudyReminderRequest request) {
        Optional<Course> courseOptional = courseJpaRepository.findById(request.getCourseID());
        Optional<User> userOptional = userJpaRepository.findById(request.getUserId());
        StudyReminder studyReminder = StudyReminder.builder()
                .course(courseOptional.get())
                .endDate(request.getEndDate())
                .eventName(request.getEventName())
                .startDate(request.getStartDate())
                .time(request.getTime())
                .user(userOptional.get())
                .frequency(request.getFrequency())
                .build();
        if (!ObjectUtils.isEmpty(request.getId())) {
            //update reminder
            StudyReminder reminderNeedUpdate = studyReminderJpaRepository.findById(request.getId()).get();
            reminderNeedUpdate.setCourse(courseOptional.get());
            reminderNeedUpdate.setEndDate(request.getEndDate());
            reminderNeedUpdate.setStartDate(request.getStartDate());
            reminderNeedUpdate.setTime(request.getTime());
            reminderNeedUpdate.setFrequency(request.getFrequency());
            reminderNeedUpdate.setEventName(request.getEventName());
            studyReminderJpaRepository.save(reminderNeedUpdate);
        }
        //create reminder
        studyReminderJpaRepository.save(studyReminder);
    }

    public void deleteReminder(Long id) {
        studyReminderJpaRepository.deleteById(id);
    }

    public List<StudyReminderResponse> getReminders(Long userId) {
        List<StudyReminder> reminders = studyReminderJpaRepository.findByUserId(userId);
        return reminders.stream().map(
                reminder -> StudyReminderResponse.builder()
                        .id(reminder.getId())
                        .frequency(reminder.getFrequency())
                        .startDate(reminder.getStartDate())
                        .endDate(reminder.getEndDate())
                        .eventName(reminder.getEventName())
                        .time(reminder.getTime())
                        .course(CourseDataResponse.builder()
                                .id(reminder.getCourse().getId())
                                .title(reminder.getCourse().getTitle())
                                .build())
                        .build()
        ).collect(Collectors.toList());
    }
}
