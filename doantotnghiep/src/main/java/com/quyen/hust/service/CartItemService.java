package com.quyen.hust.service;

import com.quyen.hust.repository.CartItemJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartItemService {
    private final CartItemJpaRepository cartItemJpaRepository;

}
