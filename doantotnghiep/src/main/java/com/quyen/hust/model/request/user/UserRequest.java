package com.quyen.hust.model.request.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quyen.hust.statics.Gender;
import com.quyen.hust.statics.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private Long id;

    @NotBlank(message = "Full name is required")
    @Length(max = 100, message = "Full name must be less than 100 characters")
    private String fullName;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate dateOfBirth;

    private Gender gender;

    @Length(max = 255, message = "Address must be less than 255 characters")
    private String address;

    @NotBlank(message = "Email is required")
    @Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$",
            message = "Email must be email format")
    @Length(max = 255, message = "Email must be less than 255 characters")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private UserStatus userStatus;

    @Length(max = 255, message = "Avatar must be less than 255 characters")
    private String avatar;

    @Pattern(regexp = "^0[0-9]{9}", message = "Phone must be 10 characters, start with zero")
    private String phone;

    @Length(max = 255, message = "Facebook url must be less than 255 characters")
    private String facebookUrl;

    @Length(max = 255, message = "Instagram url must be less than 255 characters")
    private String instagramUrl;

    @Length(max = 255, message = "Youtube url must be less than 255 characters")
    private String youtubeUrl;

    @Length(max = 255, message = "Twitter url must be less than 255 characters")
    private String twitterUrl;



}
