package com.quyen.hust.repository.user;

import com.quyen.hust.entity.user.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistItemJpaRepository extends JpaRepository<WishlistItem, Long> {


}
