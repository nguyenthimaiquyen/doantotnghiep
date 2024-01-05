package com.quyen.hust.model.request.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailUrlRequest {

    private Long id;

    @NotNull(message = "Mail sending is required")
    @Min(value = 1, message = "Mail sending ID must be positive number")
    private Long mailSendingId;

    @NotBlank(message = "Attachment url is required")
    @Length(max = 255, message = "Attachment url must be less than 255 characters")
    private String attachmentUrl;
}
