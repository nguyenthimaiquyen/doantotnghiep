package com.quyen.hust.service.user;

import com.quyen.hust.repository.user.CommentJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentJpaRepository commentJpaRepository;



}
