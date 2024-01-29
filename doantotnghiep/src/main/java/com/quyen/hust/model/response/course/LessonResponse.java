package com.quyen.hust.model.response.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonResponse {

    private Long id;

    private String title;

    private String content;

    private String embeddedUrl;

    private String videoUrl;

    private String fileUrl;

    private Long sectionId;

    private String sectionTitle;

//    private Long sectionIndex;

}
