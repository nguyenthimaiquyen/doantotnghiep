package com.quyen.hust.service.user;

import com.quyen.hust.entity.course.Course;
import com.quyen.hust.entity.user.*;
import com.quyen.hust.exception.CartItemNotFoundException;
import com.quyen.hust.exception.CourseNotFoundException;
import com.quyen.hust.exception.UserNotFoundException;
import com.quyen.hust.model.request.user.CartItemRequest;
import com.quyen.hust.repository.course.CourseJpaRepository;
import com.quyen.hust.repository.user.*;
import com.quyen.hust.service.course.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartItemService {
    private final CartItemJpaRepository cartItemJpaRepository;
    private final CourseJpaRepository courseJpaRepository;
    private final CartJpaRepository cartJpaRepository;
    private final WishlistItemJpaRepository wishlistItemJpaRepository;
    private final WishlistJpaRepository wishlistJpaRepository;

    public void createCartItem(CartItemRequest request) throws CourseNotFoundException {
        Optional<Course> courseOptional = courseJpaRepository.findById(request.getCourseId());
        if (!courseOptional.isPresent()) {
            throw new CourseNotFoundException("Course with id " + request.getCourseId() + " could not be found!");
        }
        //kiểm tra course nằm trong wishlist item thì xóa wishlist item đó đi
        Wishlist wishlist = wishlistJpaRepository.findByUserId(request.getUserId());
        List<WishlistItem> wishlistItems = wishlistItemJpaRepository.findByWishlistId(wishlist.getId());
        List<WishlistItem> removedWishlistItems = wishlistItems.stream().filter(
                wishlistItem -> wishlistItem.getCourse().getId().equals(request.getCourseId())
        ).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(removedWishlistItems)) {
            wishlistItemJpaRepository.deleteAll(removedWishlistItems);
        }

        Cart cart = cartJpaRepository.findByUserId(request.getUserId());
        List<CartItem> cartItems = cartItemJpaRepository.findByCart(cart);
        for (CartItem cartItem: cartItems) {
            if (cartItem.getCourse().getId() == courseOptional.get().getId()) {
                return;
            }
        }
        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .course(courseOptional.get())
                .build();
        cartItemJpaRepository.save(cartItem);
    }


    public void deleteCartItem(Long cartItemId) {
        cartItemJpaRepository.deleteById(cartItemId);
    }


}
