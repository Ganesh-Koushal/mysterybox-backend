package com.mysterybox.dto;

import com.mysterybox.entity.Difficulty;
import java.time.LocalDateTime;

public class CaseSummaryDto {
    private Long id;
    private String title;
    private String description;
    private Difficulty difficulty;
    private boolean isActive;
    private boolean isSolved;
    private LocalDateTime createdAt;

    public CaseSummaryDto() {}

    public CaseSummaryDto(Long id, String title, String description, Difficulty difficulty, boolean isActive, boolean isSolved, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.isActive = isActive;
        this.isSolved = isSolved;
        this.createdAt = createdAt;
    }

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

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
