package com.rikkei.backend.controller;

import com.rikkei.backend.entity.Prediction;
import com.rikkei.backend.service.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/predictions")
@RequiredArgsConstructor
public class PredictionController {

    private final PredictionService predictionService;

    @PostMapping
    public ResponseEntity<Prediction> submitPrediction(@RequestBody Map<String, Object> payload) {
        String userEmail = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        Long matchId = Long.valueOf(payload.get("matchId").toString());
        String predictedValue = payload.get("predictedValue").toString();

        return ResponseEntity.ok(predictionService.submitPrediction(userEmail, matchId, predictedValue));
    }

    @GetMapping("/my")
    public ResponseEntity<java.util.List<Prediction>> getMyPredictions() {
        String userEmail = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(predictionService.getMyPredictions(userEmail));
    }
}
