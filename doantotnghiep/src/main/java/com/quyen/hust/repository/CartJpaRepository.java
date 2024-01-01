package com.quyen.hust.repository;

import com.quyen.hust.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartJpaRepository extends JpaRepository<Cart, Long> {


}
