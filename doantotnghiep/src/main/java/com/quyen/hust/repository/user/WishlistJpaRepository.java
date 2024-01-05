package com.quyen.hust.repository.user;

import com.quyen.hust.entity.user.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistJpaRepository extends JpaRepository<Wishlist, Long> {
}
