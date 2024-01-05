package com.quyen.hust.model.response.user;

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
public class CommentResponse {

    private Long id;

    private String content;

    private Long likeNumber;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
