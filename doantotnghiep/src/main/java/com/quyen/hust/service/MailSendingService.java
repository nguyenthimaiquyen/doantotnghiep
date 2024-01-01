package com.quyen.hust.service;

import com.quyen.hust.repository.MailSendingJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailSendingService {
    private final MailSendingJpaRepository mailSendingJpaRepository;



}
