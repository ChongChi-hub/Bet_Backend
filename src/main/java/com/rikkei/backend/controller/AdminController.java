package com.rikkei.backend.controller;

import com.rikkei.backend.entity.Match;
import com.rikkei.backend.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import com.rikkei.backend.repository.PredictionRepository;

@RestController
@RequestMapping("/api/admin/matches")
@RequiredArgsConstructor
public class AdminController {

    private final MatchService matchService;
    private final PredictionRepository predictionRepository;

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

    @GetMapping("/{id}/predictions")
    public ResponseEntity<?> getMatchPredictions(@PathVariable Long id) {
        var predictions = predictionRepository.findByMatchId(id);
        var result = predictions.stream().map(p -> Map.of(
            "userName", p.getUser().getFullName(),
            "predictedValue", p.getPredictedValue()
        )).toList();
        return ResponseEntity.ok(result);
    }
}
