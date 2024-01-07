package com.quyen.hust.entity.user;

import com.quyen.hust.entity.BaseEntity;
import com.quyen.hust.entity.admin.Role;
import com.quyen.hust.statics.Gender;
import com.quyen.hust.statics.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private String address;

    @Column
    @Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Please enter a valid email address")
    private String email;

    @Column
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column
    private String avatar;

    @Column(length = 10)
    private String phone;

    @Column(name = "facebook_url")
    private String facebookUrl;

    @Column(name = "instagram_url")
    private String instagramUrl;

    @Column(name = "youtube_url")
    private String youtubeUrl;

    @Column(name = "twitter_url")
    private String twitterUrl;

    @Column(name = "deleted_date_time")
    private LocalDateTime deletedDateTime;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();



}
