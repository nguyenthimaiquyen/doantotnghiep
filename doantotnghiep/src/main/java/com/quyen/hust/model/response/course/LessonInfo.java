package com.quyen.hust.model.response.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonInfo {

    private Long courseId;

    private Long lessonId;

    private Long quizId;

}
