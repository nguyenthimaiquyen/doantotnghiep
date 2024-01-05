package com.quyen.hust.service.user;

import com.quyen.hust.repository.user.CartJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartService {
    private final CartJpaRepository cartJpaRepository;


}
