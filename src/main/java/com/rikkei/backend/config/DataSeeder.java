package com.rikkei.backend.config;

import com.rikkei.backend.entity.Match;
import com.rikkei.backend.entity.User;
import com.rikkei.backend.repository.MatchRepository;
import com.rikkei.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final com.rikkei.backend.repository.PredictionRepository predictionRepository;

    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin").isEmpty()) {
            User admin = new User();
            admin.setFullName("Admin Manager");
            admin.setEmail("admin");
            admin.setPassword(passwordEncoder.encode("123123"));
            admin.setRole(com.rikkei.backend.entity.Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Seeded admin / 123123");
        } else {
            System.out.println("Admin already exists. Skipping seed.");
        }
    }
}
