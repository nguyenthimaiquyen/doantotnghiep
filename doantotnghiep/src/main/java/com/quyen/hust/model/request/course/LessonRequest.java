package com.quyen.hust.model.request.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonRequest {

    private Long id;

    @NotNull(message = "Section is required")
    @Min(value = 1, message = "Section ID must be positive number")
    private Long sectionId;

    @NotBlank(message = "Title is required")
    @Length(max = 255, message = "Name must be less than 255 characters")
    private String title;

    @Length(max = 1000, message = "Content must be less than 1000 characters")
    private String content;

    @Length(max = 1000, message = "Embedded Url must be less than 1000 characters")
    private String embeddedUrl;

    @Length(max = 1000, message = "Video Url must be less than 1000 characters")
    private String videoUrl;

    @Length(max = 1000, message = "File Url must be less than 1000 characters")
    private String fileUrl;


}
