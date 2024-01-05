package com.quyen.hust.service.user;

import com.quyen.hust.repository.user.CartItemJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartItemService {
    private final CartItemJpaRepository cartItemJpaRepository;

}
