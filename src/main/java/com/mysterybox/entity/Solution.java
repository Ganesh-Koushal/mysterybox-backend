package com.mysterybox.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "solutions")
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", nullable = false, unique = true)
    @JsonIgnore
    private MysteryCase mysteryCase;

    @Column(name = "correct_answer", nullable = false, columnDefinition = "TEXT")
    @JsonIgnore
    private String correctAnswer;

    public Solution() {}

    public Solution(MysteryCase mysteryCase, String correctAnswer) {
        this.mysteryCase = mysteryCase;
        this.correctAnswer = correctAnswer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MysteryCase getMysteryCase() {
        return mysteryCase;
    }

    public void setMysteryCase(MysteryCase mysteryCase) {
        this.mysteryCase = mysteryCase;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
