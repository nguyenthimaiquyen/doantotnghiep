package com.quyen.hust.model.response.user;

import com.quyen.hust.model.response.course.CourseDataResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponse {

    private Long id;

    private Long cartId;

    private CourseDataResponse course;


}
