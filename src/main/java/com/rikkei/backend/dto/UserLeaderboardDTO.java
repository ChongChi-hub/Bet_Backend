package com.rikkei.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLeaderboardDTO {
    private Long id;
    private String fullName;
    private long correctPredictions;
    private BigDecimal totalPrizeWon; // this could be totalBalance
}
