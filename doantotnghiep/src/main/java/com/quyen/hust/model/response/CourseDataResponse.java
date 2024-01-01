package com.quyen.hust.model.response;

import com.quyen.hust.statics.DifficultyLevel;
import com.quyen.hust.statics.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDataResponse {

    private Long id;

    private String title;

    private String description;

    private String learningObjectives;

    private Double courseFee;

    private String courseFeeUnit;

    private Status status;

    private Integer totalLessons;

    private Long totalVideoDuration;

    private Long totalTest;

    private DifficultyLevel difficultyLevel;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private Long totalRecord;

}
