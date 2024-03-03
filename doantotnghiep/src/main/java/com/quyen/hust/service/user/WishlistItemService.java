package com.quyen.hust.service.user;

import com.quyen.hust.entity.course.Course;
import com.quyen.hust.entity.user.Cart;
import com.quyen.hust.entity.user.CartItem;
import com.quyen.hust.entity.user.Wishlist;
import com.quyen.hust.entity.user.WishlistItem;
import com.quyen.hust.exception.CourseNotFoundException;
import com.quyen.hust.model.request.user.WishlistItemRequest;
import com.quyen.hust.repository.course.CourseJpaRepository;
import com.quyen.hust.repository.user.CartItemJpaRepository;
import com.quyen.hust.repository.user.CartJpaRepository;
import com.quyen.hust.repository.user.WishlistItemJpaRepository;
import com.quyen.hust.repository.user.WishlistJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WishlistItemService {
    private final WishlistItemJpaRepository wishlistItemJpaRepository;
    private final WishlistJpaRepository wishlistJpaRepository;
    private final CourseJpaRepository courseJpaRepository;
    private final CartItemJpaRepository cartItemJpaRepository;
    private final CartJpaRepository cartJpaRepository;


    public void createWishlistItem(WishlistItemRequest request) throws CourseNotFoundException {
        Optional<Course> courseOptional = courseJpaRepository.findById(request.getCourseId());
        if (!courseOptional.isPresent()) {
            throw new CourseNotFoundException("Course with id " + request.getCourseId() + " could not be found!");
        }
        //kiểm tra course nằm trong cart item thì xóa cart item đó đi
        Cart cart = cartJpaRepository.findByUserId(request.getUserId());
        List<CartItem> cartItems = cartItemJpaRepository.findByCartId(cart.getId());
        List<CartItem> removedCartItems = cartItems
                .stream()
                .filter(cartItem -> cartItem.getCourse().getId().equals(request.getCourseId()))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(removedCartItems)) {
            cartItemJpaRepository.deleteAll(removedCartItems);
        }

        Wishlist wishlist = wishlistJpaRepository.findByUserId(request.getUserId());
        List<WishlistItem> wishlistItems = wishlistItemJpaRepository.findByWishlist(wishlist);
        for (WishlistItem wishlistItem : wishlistItems) {
            if (wishlistItem.getCourse().getId().equals(courseOptional.get().getId())) {
                return;
            }
        }
        WishlistItem wishlistItem = WishlistItem.builder()
                .wishlist(wishlist)
                .course(courseOptional.get())
                .build();
        wishlistItemJpaRepository.save(wishlistItem);
    }

    public void deleteWishlistItem(Long wishlistItemId) {
        wishlistItemJpaRepository.deleteById(wishlistItemId);
    }


}
