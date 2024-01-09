package com.quyen.hust.model.response.user;

import com.quyen.hust.model.response.course.CourseDataResponse;
import com.quyen.hust.statics.Frequency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudyReminderResponse {

    private Long id;

    private String eventName;

    private Frequency frequency;

    private LocalTime time;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private CourseDataResponse course;

    private UserResponse user;
}
