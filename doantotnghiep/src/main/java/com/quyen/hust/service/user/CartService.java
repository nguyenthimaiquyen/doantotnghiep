package com.quyen.hust.service.user;

import com.quyen.hust.entity.user.Cart;
import com.quyen.hust.entity.user.CartItem;
import com.quyen.hust.model.response.course.CourseDataResponse;
import com.quyen.hust.model.response.user.CartItemResponse;
import com.quyen.hust.model.response.user.CartResponse;
import com.quyen.hust.repository.user.CartItemJpaRepository;
import com.quyen.hust.repository.user.CartJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartService {
    private final CartJpaRepository cartJpaRepository;
    private final CartItemJpaRepository cartItemJpaRepository;

    public CartResponse getCart(Long userId) {
        Double totalFee = 0d;
        Cart cart = cartJpaRepository.findByUserId(userId);
        List<CartItem> cartItems = cartItemJpaRepository.findByCart(cart);
        for (CartItem cartItem : cartItems) {
            totalFee += cartItem.getCourse().getCourseFee();
        }
        List<CartItemResponse> cartItemResponses = cartItems.stream().map(
                cartItem -> CartItemResponse.builder()
                        .id(cartItem.getId())
                        .cartId(cartItem.getCart().getId())
                        .course(CourseDataResponse.builder()
                                .id(cartItem.getCourse().getId())
                                .imageUrl(cartItem.getCourse().getImageUrl())
                                .learnerCount(cartItem.getCourse().getLearnerCount())
                                .courseFeeUnit(cartItem.getCourse().getCourseFeeUnit())
                                .courseFee(cartItem.getCourse().getCourseFee())
                                .rating(cartItem.getCourse().getRating())
                                .teacherName(cartItem.getCourse().getTeacher().getUser().getFullName())
                                .title(cartItem.getCourse().getTitle())
                                .difficultyLevel(cartItem.getCourse().getDifficultyLevel())
                                .trainingFields(cartItem.getCourse().getTrainingField().stream().map(
                                        trainingField -> trainingField.getFieldName()
                                ).collect(Collectors.toList()))
                                .build())
                        .build()).collect(Collectors.toList());
        return CartResponse.builder()
                .id(cart.getId())
                .userId(userId)
                .totalFee(totalFee)
                .cartItems(cartItemResponses)
                .build();
    }

    public void deleteAllCartItem(Long cartId) {
        List<CartItem> cartItems = cartItemJpaRepository.findByCartId(cartId);
        cartItemJpaRepository.deleteAll(cartItems);
    }


}
