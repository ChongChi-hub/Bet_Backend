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
    public Prediction submitPrediction(String username, Long matchId, String predictedValue) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Trận đấu không tồn tại"));

        if (match.getStatus() != MatchStatus.OPEN) {
            throw new RuntimeException("Trận đấu đã bị khóa hoặc đã chốt kết quả");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        Optional<Prediction> existing = predictionRepository.findByUserIdAndMatchId(user.getId(), matchId);
        if (existing.isPresent()) {
            Prediction p = existing.get();
            p.setPredictedValue(predictedValue);
            return predictionRepository.save(p);
        }

        Prediction p = new Prediction();
        p.setMatch(match);
        p.setUser(user);
        p.setPredictedValue(predictedValue);
        return predictionRepository.save(p);
    }

    public java.util.List<Prediction> getMyPredictions(String username) {
        return predictionRepository.findByUserUsername(username);
    }
}
