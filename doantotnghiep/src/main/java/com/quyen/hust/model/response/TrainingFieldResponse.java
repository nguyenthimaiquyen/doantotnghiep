package com.quyen.hust.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingFieldResponse {

    private Long id;

    private String fieldName;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
