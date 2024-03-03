package com.quyen.hust.model.request.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoDurationRequest {

    private Long lessonId;

    private String videoDuration;
}
