package com.quyen.hust.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtResponse {

    String jwt;

    String refreshToken;

    Long id;

    String email;

    Set<String> roles;

    String fullName;

    String avatar;

}
