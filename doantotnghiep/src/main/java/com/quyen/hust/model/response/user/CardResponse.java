package com.quyen.hust.model.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardResponse {

    private Long id;

    private String nameOnCard;

    private String cardNumber;

    private String cvv;

    private LocalDate expiredAt;


}
