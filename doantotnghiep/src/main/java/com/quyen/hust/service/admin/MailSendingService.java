package com.quyen.hust.service.admin;

import com.quyen.hust.repository.admin.MailSendingJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailSendingService {
    private final MailSendingJpaRepository mailSendingJpaRepository;



}
