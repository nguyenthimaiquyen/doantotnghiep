package com.quyen.hust.service.user;

import com.quyen.hust.repository.user.WishlistItemJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WishlistItemService {
    private final WishlistItemJpaRepository wishlistItemJpaRepository;


}
