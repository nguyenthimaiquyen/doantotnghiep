package com.quyen.hust.model.response.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseResponse {
    private List<CourseDataResponse> courses;
    private Long totalElement;
    private int totalPage;
    private int currentPage;
    private int pageSize;
}
