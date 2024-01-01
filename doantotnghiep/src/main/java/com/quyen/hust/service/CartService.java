package com.quyen.hust.service;

import com.quyen.hust.repository.CartJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartService {
    private final CartJpaRepository cartJpaRepository;


}
