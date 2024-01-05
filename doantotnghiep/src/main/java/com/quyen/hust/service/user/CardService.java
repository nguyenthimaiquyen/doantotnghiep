package com.quyen.hust.service.user;

import com.quyen.hust.repository.user.CardJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CardService {
    private final CardJpaRepository cardJpaRepository;


}
