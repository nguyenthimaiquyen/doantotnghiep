package com.quyen.hust.service.user;

import com.quyen.hust.repository.user.CertificationJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CertificationService {
    private final CertificationJpaRepository certificationJpaRepository;

}
