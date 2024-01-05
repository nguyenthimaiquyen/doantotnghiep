package com.quyen.hust.repository.user;

import com.quyen.hust.entity.user.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingJpaRepository extends JpaRepository<Rating, Long> {
}
