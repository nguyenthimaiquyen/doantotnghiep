package com.quyen.hust.repository;

import com.quyen.hust.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingJpaRepository extends JpaRepository<Rating, Long> {
}
