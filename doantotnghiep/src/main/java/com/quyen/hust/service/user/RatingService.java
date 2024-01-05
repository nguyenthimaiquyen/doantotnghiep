package com.quyen.hust.service.user;

import com.quyen.hust.repository.user.RatingJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RatingService {
    private final RatingJpaRepository ratingJpaRepository;



}
