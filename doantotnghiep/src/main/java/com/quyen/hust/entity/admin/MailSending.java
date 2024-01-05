package com.quyen.hust.entity.admin;

import com.quyen.hust.entity.BaseEntity;
import com.quyen.hust.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "mail_sendings ")
public class MailSending extends BaseEntity {

    @Column
    private String subject;

    @Column
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(name = "mail_recipients",
            joinColumns = @JoinColumn(name = "mail_sending_id"),
            inverseJoinColumns = @JoinColumn(name = "recipient_id"))
    private List<User> recipients;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;



}
