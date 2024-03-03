package com.quyen.hust.service.user;

import com.quyen.hust.entity.user.Wishlist;
import com.quyen.hust.entity.user.WishlistItem;
import com.quyen.hust.model.response.course.CourseDataResponse;
import com.quyen.hust.model.response.user.WishlistItemResponse;
import com.quyen.hust.model.response.user.WishlistResponse;
import com.quyen.hust.repository.user.WishlistItemJpaRepository;
import com.quyen.hust.repository.user.WishlistJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WishlistService {
    private final WishlistJpaRepository wishlistJpaRepository;
    private final WishlistItemJpaRepository wishlistItemJpaRepository;

    public WishlistResponse getWishlist(Long userId) {
        Wishlist wishlist = wishlistJpaRepository.findByUserId(userId);
        List<WishlistItem> wishlistItems = wishlistItemJpaRepository.findByWishlist(wishlist);
        List<WishlistItemResponse> wishlistItemResponses = wishlistItems.stream().map(
                wishlistItem -> WishlistItemResponse.builder()
                        .id(wishlistItem.getId())
                        .wishlistId(wishlistItem.getWishlist().getId())
                        .course(CourseDataResponse.builder()
                                .id(wishlistItem.getCourse().getId())
                                .imageUrl(wishlistItem.getCourse().getImageUrl())
                                .learnerCount(wishlistItem.getCourse().getLearnerCount())
                                .courseFeeUnit(wishlistItem.getCourse().getCourseFeeUnit())
                                .courseFee(wishlistItem.getCourse().getCourseFee())
                                .rating(wishlistItem.getCourse().getRating())
                                .teacherName(wishlistItem.getCourse().getTeacher().getUser().getFullName())
                                .title(wishlistItem.getCourse().getTitle())
                                .difficultyLevel(wishlistItem.getCourse().getDifficultyLevel())
                                .trainingFields(wishlistItem.getCourse().getTrainingField().stream().map(
                                        trainingField -> trainingField.getFieldName()
                                ).collect(Collectors.toList()))
                                .build())
                        .build()).collect(Collectors.toList());
        return WishlistResponse.builder()
                .id(wishlist.getId())
                .userId(userId)
                .wishlistItems(wishlistItemResponses)
                .build();
    }
}
