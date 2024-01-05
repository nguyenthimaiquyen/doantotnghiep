package com.quyen.hust.service.admin;

import com.quyen.hust.repository.admin.TrainingFieldJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TrainingFieldService {
    private final TrainingFieldJpaRepository trainingFieldJpaRepository;


}
