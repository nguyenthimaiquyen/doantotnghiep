package com.quyen.hust.entity;


import com.quyen.hust.entity.user.User;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken extends BaseEntity {

    @JoinColumn(name = "user_id")
    @ManyToOne(targetEntity = User.class)
    private User user;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(columnDefinition = "boolean default false")
    private boolean invalidated;

}
