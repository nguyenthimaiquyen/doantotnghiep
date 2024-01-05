package com.quyen.hust.service.admin;

import com.quyen.hust.repository.admin.MailUrlJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailUrlService {
    private final MailUrlJpaRepository mailUrlJpaRepository;


}
