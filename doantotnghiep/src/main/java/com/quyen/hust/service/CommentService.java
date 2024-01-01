package com.quyen.hust.service;

import com.quyen.hust.repository.CommentJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentJpaRepository commentJpaRepository;



}
