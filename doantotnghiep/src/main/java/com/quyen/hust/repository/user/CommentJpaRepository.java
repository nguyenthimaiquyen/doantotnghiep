package com.quyen.hust.repository.user;

import com.quyen.hust.entity.user.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
}
