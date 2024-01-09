package com.quyen.hust.model.response.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SectionResponse {

    private Long id;

    private String title;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
