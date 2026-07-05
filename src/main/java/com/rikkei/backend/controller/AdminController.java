package com.rikkei.backend.controller;

import com.rikkei.backend.entity.Match;
import com.rikkei.backend.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/matches")
@RequiredArgsConstructor
public class AdminController {

    private final MatchService matchService;

    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody Match match) {
        return ResponseEntity.ok(matchService.createMatch(match));
    }

    @PutMapping("/{id}/lock")
    public ResponseEntity<Match> lockMatch(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.lockMatch(id));
    }

    @PutMapping("/{id}/settle")
    public ResponseEntity<Match> settleMatch(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String finalResult = payload.get("finalResult");
        String finalNote = payload.get("finalNote");
        return ResponseEntity.ok(matchService.settleMatch(id, finalResult, finalNote));
    }
}
