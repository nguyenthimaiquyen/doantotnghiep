package com.quyen.hust.repository;


import com.quyen.hust.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
