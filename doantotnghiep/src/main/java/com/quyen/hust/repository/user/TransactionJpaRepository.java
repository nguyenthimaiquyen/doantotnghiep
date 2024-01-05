package com.quyen.hust.repository.user;

import com.quyen.hust.entity.user.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionJpaRepository extends JpaRepository<Transaction, Long> {
}
