package com.quyen.hust.model.request.course;

import com.quyen.hust.statics.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseStatusRequest {

    private CourseStatus courseStatus;

}
