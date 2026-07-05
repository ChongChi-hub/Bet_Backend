package com.rikkei.backend.repository;

import com.rikkei.backend.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    List<Prediction> findByMatchId(Long matchId);
    Optional<Prediction> findByUserIdAndMatchId(Long userId, Long matchId);
    List<Prediction> findByMatchIdAndPredictedValue(Long matchId, String predictedValue);
    List<Prediction> findByUserUsername(String username);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(p) FROM Prediction p WHERE p.user.id = :userId AND p.match.status = 'SETTLED' AND p.predictedValue = p.match.finalResult")
    long countCorrectPredictionsByUserId(@org.springframework.data.repository.query.Param("userId") Long userId);
}
