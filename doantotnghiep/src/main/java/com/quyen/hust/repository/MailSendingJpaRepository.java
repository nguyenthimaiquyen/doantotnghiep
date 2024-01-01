package com.quyen.hust.repository;

import com.quyen.hust.entity.MailSending;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailSendingJpaRepository extends JpaRepository<MailSending, Long> {
}
