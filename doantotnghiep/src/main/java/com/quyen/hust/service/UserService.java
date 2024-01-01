package com.quyen.hust.service;

import com.quyen.hust.repository.UserJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserJpaRepository userJpaRepository;



}
