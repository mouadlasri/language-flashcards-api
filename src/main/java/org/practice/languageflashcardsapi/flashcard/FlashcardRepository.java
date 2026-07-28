package org.practice.languageflashcardsapi.flashcard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface FlashcardRepository extends JpaRepository<Flashcard, UUID> {
    @Query(value = "SELECT f FROM Flashcard f JOIN FETCH f.deck d WHERE d.id = :deckId AND d.deletedAt IS NULL AND f.deletedAt IS NULL",
    countQuery = "SELECT COUNT(f) FROM Flashcard f WHERE f.deck.id = :deckId AND f.deck.deletedAt IS NULL AND f.deletedAt IS NULL")
    Page<Flashcard> findAllFlashcardsWithDeckByDeckIdAndDeletedAtIsNull(@Param("deckId") UUID deckId, Pageable pageable);

    Optional<Flashcard> findByIdAndDeck_IdAndDeletedAtIsNull(UUID flashcardId, UUID deckId);
}
