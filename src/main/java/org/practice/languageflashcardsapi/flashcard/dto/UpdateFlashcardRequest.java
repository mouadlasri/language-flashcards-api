package org.practice.languageflashcardsapi.flashcard.dto;

public class UpdateFlashcardRequest {
    private String frontText;
    private String backText;

    public UpdateFlashcardRequest() {}

    public UpdateFlashcardRequest(String frontText, String backText) {
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