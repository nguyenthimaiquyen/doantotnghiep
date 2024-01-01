package com.quyen.hust.service;

import com.quyen.hust.repository.CardJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CardService {
    private final CardJpaRepository cardJpaRepository;


}
