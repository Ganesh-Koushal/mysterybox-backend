package com.mysterybox.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cases")
public class MysteryCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Difficulty difficulty = Difficulty.MEDIUM;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "mysteryCase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("displayOrder ASC")
    private List<Clue> clues = new ArrayList<>();

    @OneToMany(mappedBy = "mysteryCase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Evidence> evidenceList = new ArrayList<>();

    @OneToOne(mappedBy = "mysteryCase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Solution solution;

    public MysteryCase() {}

    public MysteryCase(String title, String description, Difficulty difficulty, boolean isActive) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty != null ? difficulty : Difficulty.MEDIUM;
        this.isActive = isActive;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.difficulty == null) {
            this.difficulty = Difficulty.MEDIUM;
        }
    }

    public void addClue(Clue clue) {
        clues.add(clue);
        clue.setMysteryCase(this);
    }

    public void removeClue(Clue clue) {
        clues.remove(clue);
        clue.setMysteryCase(null);
    }

    public void addEvidence(Evidence evidence) {
        evidenceList.add(evidence);
        evidence.setMysteryCase(this);
    }

    public void removeEvidence(Evidence evidence) {
        evidenceList.remove(evidence);
        evidence.setMysteryCase(null);
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
        if (solution != null) {
            solution.setMysteryCase(this);
        }
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Clue> getClues() {
        return clues;
    }

    public void setClues(List<Clue> clues) {
        this.clues = clues;
    }

    public List<Evidence> getEvidenceList() {
        return evidenceList;
    }

    public void setEvidenceList(List<Evidence> evidenceList) {
        this.evidenceList = evidenceList;
    }

    public Solution getSolution() {
        return solution;
    }
}
