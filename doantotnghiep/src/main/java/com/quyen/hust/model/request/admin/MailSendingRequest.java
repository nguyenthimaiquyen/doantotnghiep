package com.quyen.hust.model.request.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailSendingRequest {

    private Long id;

    @NotNull(message = "Sender is required")
    @Min(value = 1, message = "Sender ID must be positive number")
    private Long senderId;

    @NotBlank(message = "Subject is required")
    @Length(max = 255, message = "Subject must be less than 255 characters")
    private String subject;

    @NotBlank(message = "Subject is required")
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;


}
