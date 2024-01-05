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
public class WishlistItemResponse {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
