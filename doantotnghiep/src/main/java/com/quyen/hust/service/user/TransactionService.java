package com.quyen.hust.service.user;

import com.quyen.hust.repository.user.TransactionJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionJpaRepository transactionJpaRepository;



}
