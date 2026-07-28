package org.practice.languageflashcardsapi.flashcard.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public class FlashcardResponse {
    private UUID id;
    private UUID deckId;
    private String deckTitle;
    private String frontText;
    private String backText;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public FlashcardResponse(UUID id, UUID deckId, String deckTitle, String frontText, String backText, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.deckId = deckId;
        this.deckTitle = deckTitle;
        this.frontText = frontText;
        this.backText = backText;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getDeckId() {
        return deckId;
    }

    public String getDeckTitle() {
        return deckTitle;
    }

    public String getFrontText() {
        return frontText;
    }

    public String getBackText() {
        return backText;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}