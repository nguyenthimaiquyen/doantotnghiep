package com.quyen.hust.service;

import com.quyen.hust.repository.MailUrlJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailUrlService {
    private final MailUrlJpaRepository mailUrlJpaRepository;


}
