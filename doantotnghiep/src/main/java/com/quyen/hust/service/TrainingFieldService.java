package com.quyen.hust.service;

import com.quyen.hust.repository.TrainingFieldJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TrainingFieldService {
    private final TrainingFieldJpaRepository trainingFieldJpaRepository;


}
