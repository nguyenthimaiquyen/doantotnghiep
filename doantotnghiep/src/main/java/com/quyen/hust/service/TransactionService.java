package com.quyen.hust.service;

import com.quyen.hust.repository.TransactionJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionJpaRepository transactionJpaRepository;



}
