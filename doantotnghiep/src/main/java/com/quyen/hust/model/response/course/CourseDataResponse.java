package com.quyen.hust.model.response.course;

import com.quyen.hust.statics.CourseStatus;
import com.quyen.hust.statics.DifficultyLevel;
import com.quyen.hust.statics.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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

    private Unit courseFeeUnit;

    private CourseStatus courseStatus;

    private List<String> trainingFields;

    private String discountCodeName;

    private String teacherName;

    private Integer totalLessons;

    private Long totalVideoDuration;

    private Long totalTest;

    private DifficultyLevel difficultyLevel;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private Long ratingValue;

    private Long totalRecord;

}
