package com.mysterybox.dto;

import com.mysterybox.entity.Difficulty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminCaseDto {
    private Long id;
    private String title;
    private String description;
    private Difficulty difficulty;
    private boolean isActive;
    private String correctAnswer;
    private LocalDateTime createdAt;
    private List<ClueDto> clues = new ArrayList<>();
    private List<EvidenceDto> evidence = new ArrayList<>();

    public AdminCaseDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
