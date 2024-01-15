package com.quyen.hust.repository.user;


import com.quyen.hust.entity.user.User;
import com.quyen.hust.statics.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query(value = "select u from User u join u.roles r where r.name like %:role%")
    List<User> findByRolesNameContaining(@Param("role") String role);


}
