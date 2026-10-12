package com.mysterybox.dto;

public class ClueDto {
    private Long id;
    private String title;
    private String content;
    private Integer displayOrder;

    public ClueDto() {}

    public ClueDto(Long id, String title, String content, Integer displayOrder) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.displayOrder = displayOrder;
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
