package com.quyen.hust.model.request.search;

import lombok.Data;

@Data
public class CourseSearchRequest extends BaseSearchRequest{

    private String courseName;

    private Double courseFeeStart;

    private Double courseFeeEnd;

    private Long teacherId;

    private String trainingFieldName;

    private Integer ratingValueStart;

    private Integer ratingValueEnd;

    private String difficultyLevel;

}
