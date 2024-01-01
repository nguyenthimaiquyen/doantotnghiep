package com.quyen.hust.service;

import com.quyen.hust.repository.RatingJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RatingService {
    private final RatingJpaRepository ratingJpaRepository;



}
