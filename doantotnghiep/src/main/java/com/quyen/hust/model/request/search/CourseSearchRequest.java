package com.quyen.hust.model.request.search;

import lombok.Data;

import java.util.List;

@Data
public class CourseSearchRequest extends BaseSearchRequest{

    private String courseName;

    private List<Double> courseFee;

    private List<Long> teacherId;

    private List<Long> trainingFieldId;

    private Double ratingValue;

    private List<String> difficultyLevel;

}
