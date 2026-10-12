package com.mysterybox.dto;

import java.util.ArrayList;
import java.util.List;

public class UserProgressDto {
    private int totalScore;
    private long solvedCasesCount;
    private long totalAvailableCases;
    private String detectiveRank;
    private List<SolvedCaseSummaryDto> solvedCases = new ArrayList<>();

    public UserProgressDto() {}

    public UserProgressDto(int totalScore, long solvedCasesCount, long totalAvailableCases, String detectiveRank, List<SolvedCaseSummaryDto> solvedCases) {
        this.totalScore = totalScore;
        this.solvedCasesCount = solvedCasesCount;
        this.totalAvailableCases = totalAvailableCases;
        this.detectiveRank = detectiveRank;
        this.solvedCases = solvedCases;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public long getSolvedCasesCount() {
        return solvedCasesCount;
    }

    public void setSolvedCasesCount(long solvedCasesCount) {
        this.solvedCasesCount = solvedCasesCount;
    }

    public long getTotalAvailableCases() {
        return totalAvailableCases;
    }

    public void setTotalAvailableCases(long totalAvailableCases) {
        this.totalAvailableCases = totalAvailableCases;
    }

    public String getDetectiveRank() {
        return detectiveRank;
    }

    public void setDetectiveRank(String detectiveRank) {
        this.detectiveRank = detectiveRank;
    }

    public List<SolvedCaseSummaryDto> getSolvedCases() {
        return solvedCases;
    }

    public void setSolvedCases(List<SolvedCaseSummaryDto> solvedCases) {
        this.solvedCases = solvedCases;
    }
}
