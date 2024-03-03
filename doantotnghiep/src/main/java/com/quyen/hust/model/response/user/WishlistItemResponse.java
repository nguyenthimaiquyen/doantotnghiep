package com.quyen.hust.model.response.user;

import com.quyen.hust.model.response.course.CourseDataResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistItemResponse {

    private Long id;

    private Long wishlistId;

    private CourseDataResponse course;

}
