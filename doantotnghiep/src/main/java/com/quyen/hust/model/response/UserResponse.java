package com.quyen.hust.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.quyen.hust.statics.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.validation.constraints.Email;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private Long id;

    private String fullName;

    private LocalDate dateOfBirth;

    private Gender gender;

    private String address;

    private String email;

    private Boolean activated;

    private String avatar;

    private String phone;

    private String facebookUrl;

    private String instagramUrl;

    private String youtubeUrl;

    private String twitterUrl;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private Set<RoleResponse> roles;


}
