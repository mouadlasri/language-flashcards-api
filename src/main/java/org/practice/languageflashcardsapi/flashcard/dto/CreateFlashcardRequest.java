package org.practice.languageflashcardsapi.flashcard.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateFlashcardRequest {
    @NotBlank
    private String frontText;

    @NotBlank
    private String backText;

    public CreateFlashcardRequest() {}

    public CreateFlashcardRequest(String frontText, String backText) {
        this.frontText = frontText;
        this.backText = backText;
    }

    public String getFrontText() {
        return frontText;
    }

    public String getBackText() {
        return backText;
    }

    public void setFrontText(String frontText) {
        this.frontText = frontText;
    }

    public void setBackText(String backText) {
        this.backText = backText;
    }
}
