package com.mysterybox.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "user_progress",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "case_id"})
    }
)
public class UserProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "case_id", nullable = false)
    private MysteryCase mysteryCase;

    @Column(name = "is_solved", nullable = false)
    private boolean isSolved = false;

    @Column(name = "score_awarded", nullable = false)
    private Integer scoreAwarded = 0;

    @Column(name = "solved_at")
    private LocalDateTime solvedAt;

    public UserProgress() {}

    public UserProgress(User user, MysteryCase mysteryCase, boolean isSolved, Integer scoreAwarded, LocalDateTime solvedAt) {
        this.user = user;
        this.mysteryCase = mysteryCase;
        this.isSolved = isSolved;
        this.scoreAwarded = scoreAwarded != null ? scoreAwarded : 0;
        this.solvedAt = solvedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MysteryCase getMysteryCase() {
        return mysteryCase;
    }

    public void setMysteryCase(MysteryCase mysteryCase) {
        this.mysteryCase = mysteryCase;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }

    public Integer getScoreAwarded() {
        return scoreAwarded;
    }

    public void setScoreAwarded(Integer scoreAwarded) {
        this.scoreAwarded = scoreAwarded;
    }

    public LocalDateTime getSolvedAt() {
        return solvedAt;
    }

    public void setSolvedAt(LocalDateTime solvedAt) {
        this.solvedAt = solvedAt;
    }
}
