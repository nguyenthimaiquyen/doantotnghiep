package com.quyen.hust.model.request.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quyen.hust.statics.Frequency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyReminderRequest {

    private Long id;

    @NotNull(message = "User is required")
    @Min(value = 1, message = "User ID must be positive number")
    private Long userId;

    @NotNull(message = "Course is required")
    @Min(value = 1, message = "Course ID must be positive number")
    private Long courseID;

    @NotBlank(message = "Event name is required")
    @Length(max = 255, message = "Event name must be less than 255 characters")
    private String eventName;

    @NotBlank(message = "Frequency is required")
    @Length(max = 20, message = "Frequency must be less than 20 characters")
    private Frequency frequency;

    @NotNull(message = "Time is required")
    @JsonFormat(pattern = "hh-mm", shape = JsonFormat.Shape.STRING)
    private LocalTime time;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate endDate;

}
