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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final PredictionRepository predictionRepository;
    private final UserRepository userRepository;

    public List<Match> getAllMatches() {
        return matchRepository.findAllByOrderByMatchTimeAsc();
    }

    public List<Match> getActiveMatches() {
        return matchRepository.findByStatusOrderByMatchTimeAsc(MatchStatus.OPEN);
    }

    public Match createMatch(Match match) {
        match.setStatus(MatchStatus.OPEN);
        return matchRepository.save(match);
    }

    @Transactional
    public Match lockMatch(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));
        
        if (match.getStatus() != MatchStatus.OPEN) {
            throw new RuntimeException("Only OPEN matches can be locked");
        }
        
        match.setStatus(MatchStatus.LOCKED);
        return matchRepository.save(match);
    }

    @Transactional
    public Match settleMatch(Long matchId, String finalResult, String finalNote) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        if (match.getStatus() == MatchStatus.SETTLED) {
            throw new RuntimeException("Match is already settled");
        }

        match.setFinalResult(finalResult);
        match.setFinalNote(finalNote);
        match.setStatus(MatchStatus.SETTLED);

        List<Prediction> winningPredictions = predictionRepository.findByMatchIdAndPredictedValue(matchId, finalResult);

        if (!winningPredictions.isEmpty()) {
            BigDecimal totalWinners = new BigDecimal(winningPredictions.size());
            BigDecimal payoutPerUser = match.getPrizePool().divide(totalWinners, 2, RoundingMode.HALF_UP);

            for (Prediction p : winningPredictions) {
                User user = p.getUser();
                user.setTotalBalance(user.getTotalBalance().add(payoutPerUser));
                userRepository.save(user);
            }
        } else {
            List<Match> openMatches = matchRepository.findByStatusOrderByMatchTimeAsc(MatchStatus.OPEN);
            if (!openMatches.isEmpty()) {
                Match nextMatch = openMatches.get(0);
                nextMatch.setPrizePool(nextMatch.getPrizePool().add(match.getPrizePool()));
                matchRepository.save(nextMatch);
            }
        }

        return matchRepository.save(match);
    }
}
