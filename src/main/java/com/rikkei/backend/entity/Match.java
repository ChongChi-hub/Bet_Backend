package com.rikkei.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Getter
@Setter
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_a", nullable = false)
    private String teamA;

    @Column(name = "team_b", nullable = false)
    private String teamB;

    @Column(name = "match_time", nullable = false)
    private LocalDateTime matchTime;

    @Column(name = "prize_pool", nullable = false)
    private BigDecimal prizePool;

    @Column(name = "match_stage")
    private String matchStage;

    @Column(name = "criterion_type")
    private String criterionType;

    @Column(name = "criterion_label")
    private String criterionLabel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status = MatchStatus.OPEN;

    @Column(name = "final_result")
    private String finalResult;

    @Column(name = "final_note", columnDefinition = "TEXT")
    private String finalNote;
}
