package com.rikkei.backend.repository;

import com.rikkei.backend.entity.Match;
import com.rikkei.backend.entity.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByStatusOrderByMatchTimeAsc(MatchStatus status);
    List<Match> findAllByOrderByMatchTimeAsc();
}
