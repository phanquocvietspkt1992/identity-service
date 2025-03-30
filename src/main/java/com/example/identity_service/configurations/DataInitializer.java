package com.example.identity_service.configurations;

import com.example.identity_service.entities.Role;
import com.example.identity_service.entities.User;
import com.example.identity_service.enums.RoleEnum;
import com.example.identity_service.repositories.RoleRepository;
import com.example.identity_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    @Bean
    public CommandLineRunner initAdminUser(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
            var adminUsername = "admin";
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

            // Ensure the "ADMIN" role exists
            Role adminRole = roleRepository.findByName(RoleEnum.ADMIN.name())
                    .orElseGet(() -> {
                        Role newRole = Role.builder().name(RoleEnum.ADMIN.name()).build();
                        return roleRepository.save(newRole);
                    });

            // Check if admin user already exists
            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                User adminUser = User.builder()
                        .firstName("Admin")
                        .lastName("User")
                        .email("admin@example.com")
                        .username(adminUsername)
                        .password(passwordEncoder.encode("123"))  // Secure password
                        .roles(Set.of(adminRole)) // Assign the actual Role object
                        .build();

                userRepository.save(adminUser);
                System.out.println("✅ Admin user created successfully!");
                log.info("✅ Admin user created successfully!");
            }
        };
    }

}