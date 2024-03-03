package com.quyen.hust.repository.user;

import com.quyen.hust.entity.user.Wishlist;
import com.quyen.hust.entity.user.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishlistItemJpaRepository extends JpaRepository<WishlistItem, Long> {

    List<WishlistItem> findByWishlist(Wishlist wishlist);

    @Query(value = "select w from WishlistItem w where w.wishlist.id = :wishlist_id")
    List<WishlistItem> findByWishlistId(@Param("wishlist_id") Long wishlistId);

}
