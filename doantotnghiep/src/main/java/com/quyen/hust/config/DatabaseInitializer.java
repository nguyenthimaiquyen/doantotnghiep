package com.quyen.hust.config;

import com.quyen.hust.entity.admin.Role;
import com.quyen.hust.entity.user.User;
import com.quyen.hust.repository.admin.RoleJpaRepository;
import com.quyen.hust.repository.user.UserJpaRepository;
import com.quyen.hust.statics.Roles;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final UserJpaRepository userJpaRepository;

    private final RoleJpaRepository roleJpaRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Optional<Role> userRoleOptional = roleJpaRepository.findByName(Roles.USER);
        if (userRoleOptional.isEmpty()) {
            Role userRole = Role.builder().name(Roles.USER).build();
            roleJpaRepository.save(userRole);

            User quyen = userJpaRepository.findByEmail("maiquyen2403@gmail.com");
            if (ObjectUtils.isEmpty(quyen)) {
                User user2 = new User();
                user2.setEmail("maiquyen2403@gmail.com");
                user2.setFullName("Nguyễn Thị Mai Quyên");
                user2.setPassword(passwordEncoder.encode("quyen")); // Encrypt the password
                Set<Role> roles2 = new HashSet<>();
                roles2.add(userRole);
                user2.setRoles(roles2);
                userJpaRepository.save(user2);
            }
        }
        Optional<Role> adminRoleOptional = roleJpaRepository.findByName(Roles.ADMIN);
        if (adminRoleOptional.isEmpty()) {
            Role adminRole = Role.builder().name(Roles.ADMIN).build();
            roleJpaRepository.save(adminRole);

            User admin = userJpaRepository.findByEmail("admin@gmail.com");
            if (ObjectUtils.isEmpty(admin)) {
                User user1 = new User();
                user1.setEmail("admin@gmail.com");
                user1.setFullName("admin");
                user1.setPassword(passwordEncoder.encode("admin")); // Encrypt the password
                Set<Role> roles1 = new HashSet<>();
                roles1.add(adminRole);
                user1.setRoles(roles1);
                userJpaRepository.save(user1);
            }
        }


    }



}
