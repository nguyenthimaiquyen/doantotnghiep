package com.quyen.hust.service;

import com.quyen.hust.repository.DiscountCodeJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DiscountCodeService {
    private final DiscountCodeJpaRepository discountCodeJpaRepository;

}
