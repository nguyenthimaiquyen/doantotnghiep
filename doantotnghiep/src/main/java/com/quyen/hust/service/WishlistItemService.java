package com.quyen.hust.service;

import com.quyen.hust.repository.WishlistItemJpaRepository;
import com.quyen.hust.repository.WishlistJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WishlistItemService {
    private final WishlistItemJpaRepository wishlistItemJpaRepository;


}
