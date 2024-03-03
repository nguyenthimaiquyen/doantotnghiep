package com.quyen.hust.model.request.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishlistItemRequest {

    @NotNull(message = "User is required")
    @Min(value = 1, message = "User ID must be positive number")
    private Long userId;

    @NotNull(message = "Course is required")
    @Min(value = 1, message = "Course ID must be positive number")
    private Long courseId;




}
