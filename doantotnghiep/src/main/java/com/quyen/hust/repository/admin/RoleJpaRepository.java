package com.quyen.hust.repository.admin;

import com.quyen.hust.entity.admin.Role;
import com.quyen.hust.statics.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(Roles name);
}
