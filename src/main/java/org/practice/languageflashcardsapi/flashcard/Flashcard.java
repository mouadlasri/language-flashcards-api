package org.practice.languageflashcardsapi.flashcard;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.practice.languageflashcardsapi.deck.Deck;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "flashcards")
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    @Column(name = "front_text", nullable = false)
    private String frontText;

    @Column(name = "back_text", nullable = false)
    private String backText;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    public Flashcard() {}

    public Flashcard(Deck deck, String frontText, String backText) {
        this.deck = deck;
        this.frontText = frontText;
        this.backText = backText;
    }

    public UUID getId() {
        return id;
    }

    public Deck getDeck() {
        return deck;
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

    public OffsetDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setFrontText(String frontText) {
        this.frontText = frontText;
    }

    public void setBackText(String backText) {
        this.backText = backText;
    }

    public void setDeletedAt(OffsetDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
