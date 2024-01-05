package com.quyen.hust.repository.admin;

import com.quyen.hust.entity.admin.MailSending;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailSendingJpaRepository extends JpaRepository<MailSending, Long> {
}
