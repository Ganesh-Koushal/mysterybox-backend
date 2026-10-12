package com.mysterybox.dto;

public class SubmitAnswerResponse {
    private boolean correct;
    private boolean alreadySolved;
    private int scoreAwarded;
    private int totalScore;
    private String message;

    public SubmitAnswerResponse() {}

    public SubmitAnswerResponse(boolean correct, boolean alreadySolved, int scoreAwarded, int totalScore, String message) {
        this.correct = correct;
        this.alreadySolved = alreadySolved;
        this.scoreAwarded = scoreAwarded;
        this.totalScore = totalScore;
        this.message = message;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public boolean isAlreadySolved() {
        return alreadySolved;
    }

    public void setAlreadySolved(boolean alreadySolved) {
        this.alreadySolved = alreadySolved;
    }

    public int getScoreAwarded() {
        return scoreAwarded;
    }

    public void setScoreAwarded(int scoreAwarded) {
        this.scoreAwarded = scoreAwarded;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
