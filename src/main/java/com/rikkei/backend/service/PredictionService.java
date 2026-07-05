package com.rikkei.backend.service;

import com.rikkei.backend.entity.Match;
import com.rikkei.backend.entity.MatchStatus;
import com.rikkei.backend.entity.Prediction;
import com.rikkei.backend.entity.User;
import com.rikkei.backend.repository.MatchRepository;
import com.rikkei.backend.repository.PredictionRepository;
import com.rikkei.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PredictionService {

    private final PredictionRepository predictionRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;

    @Transactional
    public Prediction submitPrediction(String email, Long matchId, String predictedValue) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        if (match.getStatus() != MatchStatus.OPEN) {
            throw new RuntimeException("Match is no longer OPEN for predictions");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Prediction> existingOpt = predictionRepository.findByUserIdAndMatchId(user.getId(), matchId);
        if (existingOpt.isPresent()) {
            Prediction existing = existingOpt.get();
            existing.setPredictedValue(predictedValue);
            return predictionRepository.save(existing);
        } else {
            Prediction newPrediction = new Prediction();
            newPrediction.setMatch(match);
            newPrediction.setUser(user);
            newPrediction.setPredictedValue(predictedValue);
            return predictionRepository.save(newPrediction);
        }
    }

    @Transactional(readOnly = true)
    public java.util.List<Prediction> getMyPredictions(String email) {
        return predictionRepository.findByUserEmail(email);
    }
}
