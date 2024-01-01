package com.quyen.hust.service;

import com.quyen.hust.repository.CertificationJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CertificationService {
    private final CertificationJpaRepository certificationJpaRepository;

}
