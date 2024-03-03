package com.quyen.hust.repository.user;

import com.quyen.hust.entity.user.Cart;
import com.quyen.hust.entity.user.CartItem;
import com.quyen.hust.entity.user.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemJpaRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart(Cart cart);

    @Query(value = "select c from CartItem c where c.cart.id = :cart_id")
    List<CartItem> findByCartId(@Param("cart_id") Long cartId);


}
