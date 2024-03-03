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

    private Double rating;

    private Long learnerCount;

    private String imageUrl;

    private List<Long> trainingFieldsId;

    private List<String> trainingFields;

    private Long discountCodeId;

    private String discountCodeName;

    private Long teacherId;

    private String teacherName;

    private Integer totalLessons;

    private String totalTime;

    private Integer totalQuizzes;

    private DifficultyLevel difficultyLevel;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private Long ratingValue;

    private Long totalRecord;

}
