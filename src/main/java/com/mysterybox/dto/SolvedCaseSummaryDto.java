package com.mysterybox.dto;

import com.mysterybox.entity.Difficulty;
import java.time.LocalDateTime;

public class SolvedCaseSummaryDto {
    private Long caseId;
    private String title;
    private Difficulty difficulty;
    private int scoreAwarded;
    private LocalDateTime solvedAt;

    public SolvedCaseSummaryDto() {}

    public SolvedCaseSummaryDto(Long caseId, String title, Difficulty difficulty, int scoreAwarded, LocalDateTime solvedAt) {
        this.caseId = caseId;
        this.title = title;
        this.difficulty = difficulty;
        this.scoreAwarded = scoreAwarded;
        this.solvedAt = solvedAt;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getScoreAwarded() {
        return scoreAwarded;
    }

    public void setScoreAwarded(int scoreAwarded) {
        this.scoreAwarded = scoreAwarded;
    }

    public LocalDateTime getSolvedAt() {
        return solvedAt;
    }

    public void setSolvedAt(LocalDateTime solvedAt) {
        this.solvedAt = solvedAt;
    }
}
