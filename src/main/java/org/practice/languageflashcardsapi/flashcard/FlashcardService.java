package org.practice.languageflashcardsapi.flashcard;

import org.practice.languageflashcardsapi.deck.Deck;
import org.practice.languageflashcardsapi.deck.DeckService;
import org.practice.languageflashcardsapi.exception.InvalidRequestException;
import org.practice.languageflashcardsapi.flashcard.dto.CreateFlashcardRequest;
import org.practice.languageflashcardsapi.flashcard.dto.FlashcardResponse;
import org.practice.languageflashcardsapi.flashcard.dto.UpdateFlashcardRequest;
import org.practice.languageflashcardsapi.flashcard.exception.FlashcardNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class FlashcardService {
    private final FlashcardRepository flashcardRepository;
    private final DeckService deckService;

    public FlashcardService(FlashcardRepository flashcardRepository, DeckService deckService) {
        this.flashcardRepository = flashcardRepository;
        this.deckService = deckService;
    }

    @Transactional(readOnly = true)
    public Page<FlashcardResponse> getAllFlashcardsByDeckId(UUID userId, UUID deckId, Pageable pageable) {
        Deck deck = deckService.getDeckEntityByIdAndUserId(deckId, userId);

        Page<Flashcard> flashcardPage = flashcardRepository.findAllFlashcardsWithDeckByDeckIdAndDeletedAtIsNull(deckId, pageable);

        Page<FlashcardResponse> flashcardResponsePage = flashcardPage.map(flashcard -> toFlashcardResponse(flashcard, deck));

        return flashcardResponsePage;
    }

    @Transactional(readOnly = true)
    public FlashcardResponse getFlashcardById(UUID userId, UUID deckId, UUID flashcardId) {
        Deck deck = deckService.getDeckEntityByIdAndUserId(deckId, userId);

        Flashcard flashcard = flashcardRepository.findByIdAndDeck_IdAndDeletedAtIsNull(flashcardId, deckId)
                .orElseThrow(() -> new FlashcardNotFoundException());

        return toFlashcardResponse(flashcard, deck);
    }

    @Transactional
    public FlashcardResponse createFlashcard(UUID userId, UUID deckId, CreateFlashcardRequest createFlashcardRequest) {
        Deck deck = deckService.getDeckEntityByIdAndUserId(deckId, userId);

        String frontText = createFlashcardRequest.getFrontText();
        String backText = createFlashcardRequest.getBackText();

        Flashcard flashcard = new Flashcard(
                deck,
                frontText,
                backText
        );

        Flashcard newFlashcard = flashcardRepository.save(flashcard);

        return toFlashcardResponse(newFlashcard, deck);
    }

    @Transactional
    public FlashcardResponse updateFlashcard(UUID userId, UUID deckId, UUID flashcardId, UpdateFlashcardRequest updateFlashcardRequest) {
        Deck deck = deckService.getDeckEntityByIdAndUserId(deckId, userId);

        Flashcard flashcard = flashcardRepository.findByIdAndDeck_IdAndDeletedAtIsNull(flashcardId, deckId)
                .orElseThrow(() -> new FlashcardNotFoundException());

        String frontText = updateFlashcardRequest.getFrontText();
        String backText = updateFlashcardRequest.getBackText();

        if (frontText != null) {
            if (frontText.isBlank()) {
                throw new InvalidRequestException("Front text must not be blank.");
            }

            if (!flashcard.getFrontText().equals(frontText)) {
                flashcard.setFrontText(frontText);
            }
        }

        if (backText != null) {
            if (backText.isBlank()) {
                throw new InvalidRequestException("Back text must not be blank.");
            }

            if (!flashcard.getBackText().equals(backText)) {
                flashcard.setBackText(backText);
            }
        }

        return toFlashcardResponse(flashcard, deck);
    }

    @Transactional
    public void deletedFlashcard(UUID userId, UUID deckId, UUID flashcardId) {
        deckService.getDeckEntityByIdAndUserId(deckId, userId);

        Flashcard flashcard = flashcardRepository.findByIdAndDeck_IdAndDeletedAtIsNull(flashcardId, deckId)
                .orElseThrow(() -> new FlashcardNotFoundException());

        flashcard.setDeletedAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    private FlashcardResponse toFlashcardResponse(Flashcard flashcard, Deck deck) {
        return new FlashcardResponse(
                flashcard.getId(),
                deck.getId(),
                deck.getTitle(),
                flashcard.getFrontText(),
                flashcard.getBackText(),
                flashcard.getCreatedAt(),
                flashcard.getUpdatedAt()
        );
    }
}
