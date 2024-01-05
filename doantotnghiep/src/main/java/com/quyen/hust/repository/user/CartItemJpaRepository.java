package com.quyen.hust.repository.user;

import com.quyen.hust.entity.user.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemJpaRepository extends JpaRepository<CartItem, Long> {
}
