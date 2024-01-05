package com.quyen.hust.entity.user;

import com.quyen.hust.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cards")
public class Card extends BaseEntity {

    @Column(name = "name_on_card", length = 100)
    private String nameOnCard;

    @Column(name = "card_number", length = 50)
    private String cardNumber;

    @Column(name = "cvv", length = 50)
    private String cvv;

    @Column(name = "expired_at")
    private LocalDate expiredAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
