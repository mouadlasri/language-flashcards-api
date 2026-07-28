package org.practice.languageflashcardsapi.deck.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateDeckRequest {
    @NotBlank
    private String title;

    private String description;

    public CreateDeckRequest() {}

    public CreateDeckRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
