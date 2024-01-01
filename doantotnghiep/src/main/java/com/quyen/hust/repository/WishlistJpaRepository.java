package com.quyen.hust.repository;

import com.quyen.hust.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistJpaRepository extends JpaRepository<Wishlist, Long> {
}
