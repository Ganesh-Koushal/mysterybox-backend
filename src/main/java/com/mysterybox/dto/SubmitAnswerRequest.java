package com.mysterybox.dto;

import jakarta.validation.constraints.NotBlank;

public class SubmitAnswerRequest {

    @NotBlank(message = "Proposed solution / suspect cannot be empty")
    private String answer;

    public SubmitAnswerRequest() {}

    public SubmitAnswerRequest(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
