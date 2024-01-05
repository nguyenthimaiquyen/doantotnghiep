package com.quyen.hust.model.request.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardRequest {

    private Long id;

    @NotNull(message = "User is required")
    @Min(value = 1, message = "User ID must be positive number")
    private Long userId;

    @NotBlank(message = "Name is required")
    @Length(max = 100, message = "Name must be less than 100 characters")
    private String nameOnCard;

    @NotBlank(message = "Card number is required")
    @Length(max = 50, message = "Card number must be less than 50 digits")
    @Pattern(regexp = "^[1-9]\\d*$", message = "Card number must be digits")
    private String cardNumber;

    @NotBlank(message = "CVV is required")
    @Length(max = 3, message = "CVV must be 3 digits")
    @Pattern(regexp = "^[1-9]\\d*$", message = "CVV must be digits")
    private String cvv;

    @NotBlank(message = "Expired time is required")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate expiredAt;


}
