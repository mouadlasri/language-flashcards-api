package org.practice.languageflashcardsapi.deck;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface DeckRepository extends JpaRepository<Deck, UUID> {
    Page<Deck> findAllByUserIdAndDeletedAtIsNull(UUID userId, Pageable pageable);

    @Query("SELECT d FROM Deck d WHERE d.id = :deckId AND d.user.id = :userId AND d.deletedAt IS NULL")
    Optional<Deck> findByIdAndUserIdAndDeletedAtIsNull(@Param("deckId") UUID deckId, @Param("userId") UUID userId);
}
