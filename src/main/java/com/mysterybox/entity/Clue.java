package com.mysterybox.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "clues")
public class Clue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", nullable = false)
    @JsonIgnore
    private MysteryCase mysteryCase;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 1;

    public Clue() {}

    public Clue(MysteryCase mysteryCase, String title, String content, Integer displayOrder) {
        this.mysteryCase = mysteryCase;
        this.title = title;
        this.content = content;
        this.displayOrder = displayOrder != null ? displayOrder : 1;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
