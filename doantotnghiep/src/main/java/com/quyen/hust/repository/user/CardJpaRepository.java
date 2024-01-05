package com.quyen.hust.repository.user;

import com.quyen.hust.entity.user.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardJpaRepository extends JpaRepository<Card, Long> {
}
