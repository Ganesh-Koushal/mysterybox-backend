package com.mysterybox.dto;

import com.mysterybox.entity.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class AdminCaseRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Difficulty is required")
    private Difficulty difficulty;

    private boolean isActive = true;

    @NotBlank(message = "Correct solution answer is required")
    private String correctAnswer;

    private List<ClueDto> clues = new ArrayList<>();
    private List<EvidenceDto> evidence = new ArrayList<>();

    public AdminCaseRequest() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<ClueDto> getClues() {
        return clues;
    }

    public void setClues(List<ClueDto> clues) {
        this.clues = clues;
    }

    public List<EvidenceDto> getEvidence() {
        return evidence;
    }

    public void setEvidence(List<EvidenceDto> evidence) {
        this.evidence = evidence;
    }
}
