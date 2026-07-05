package com.rikkei.backend.controller;

import com.rikkei.backend.entity.User;
import com.rikkei.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<com.rikkei.backend.dto.UserLeaderboardDTO>> getLeaderboard() {
        return ResponseEntity.ok(userService.getLeaderboard());
    }
}
