package com.rikkei.backend.service;

import com.rikkei.backend.entity.User;
import com.rikkei.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.rikkei.backend.entity.Role;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final com.rikkei.backend.repository.PredictionRepository predictionRepository;

    public List<com.rikkei.backend.dto.UserLeaderboardDTO> getLeaderboard() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "totalBalance"))
            .stream()
            .filter(user -> user.getRole() != Role.ADMIN)
            .map(user -> {
                long correctCount = predictionRepository.countCorrectPredictionsByUserId(user.getId());
                return new com.rikkei.backend.dto.UserLeaderboardDTO(
                    user.getId(),
                    user.getFullName(),
                    correctCount,
                    user.getTotalBalance()
                );
            })
            .toList();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        if (user.getTotalBalance() == null) {
            user.setTotalBalance(java.math.BigDecimal.ZERO);
        }
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(passwordEncoder.encode("123456")); // default password
        }
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        return userRepository.save(user);
    }

    public User updateUser(Long id, User details) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFullName(details.getFullName());
        if (details.getRole() != null) {
            user.setRole(details.getRole());
        }
        if (details.getPassword() != null && !details.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(details.getPassword()));
        }
        if (details.getTotalBalance() != null) {
            user.setTotalBalance(details.getTotalBalance());
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
