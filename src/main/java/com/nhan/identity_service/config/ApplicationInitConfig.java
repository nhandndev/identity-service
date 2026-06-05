package com.nhan.identity_service.config;

import com.nhan.identity_service.entity.Users;
import com.nhan.identity_service.enums.Role;
import com.nhan.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @ConditionalOnProperty(
            name = "app.init-data",
            havingValue = "true",
            matchIfMissing = true
    )
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByuserName("admin").isEmpty()) {
                HashSet<Role> roles = new HashSet<>();
                roles.add(Role.ADMIN);
                Users user = Users.builder().userName("admin").password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .email("admin@gmail.com")
                        .lastName("admin")
                        .firstName("admin")
                        .build();
                userRepository.save(user);
                log.warn("Admin has been created with UserName : {}", user.getUserName());
            }
        };
    }
}