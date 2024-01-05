package com.quyen.hust.repository.user;


import com.quyen.hust.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
