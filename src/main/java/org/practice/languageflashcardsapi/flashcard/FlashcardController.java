package org.practice.languageflashcardsapi.flashcard;

import jakarta.validation.Valid;
import org.practice.languageflashcardsapi.flashcard.dto.CreateFlashcardRequest;
import org.practice.languageflashcardsapi.flashcard.dto.FlashcardResponse;
import org.practice.languageflashcardsapi.flashcard.dto.UpdateFlashcardRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/decks/{deckId}/flashcards")
public class FlashcardController {
    private final FlashcardService flashcardService;

    public FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    @GetMapping
    public ResponseEntity<Page<FlashcardResponse>> getAllFlashcardsByDeckId(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID deckId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        UUID userId = UUID.fromString(jwt.getSubject());

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<FlashcardResponse> flashcardResponsePage = flashcardService.getAllFlashcardsByDeckId(userId, deckId, pageable);

        return ResponseEntity.ok(flashcardResponsePage);
    }


    @GetMapping("/{flashcardId}")
    public ResponseEntity<FlashcardResponse> getFlashcardById(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID deckId, @PathVariable UUID flashcardId) {
        UUID userId = UUID.fromString(jwt.getSubject());

        FlashcardResponse flashcardResponse = flashcardService.getFlashcardById(userId, deckId, flashcardId);

        return ResponseEntity.ok(flashcardResponse);
    }

    @PostMapping
    public ResponseEntity<FlashcardResponse> createFlashcard(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID deckId, @Valid @RequestBody CreateFlashcardRequest createFlashcardRequest) {
        UUID userId = UUID.fromString(jwt.getSubject());

        FlashcardResponse flashcardResponse = flashcardService.createFlashcard(userId, deckId, createFlashcardRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(flashcardResponse);
    }

    @PatchMapping("/{flashcardId}")
    public ResponseEntity<FlashcardResponse> updateFlashcard(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID deckId, @PathVariable UUID flashcardId, @Valid @RequestBody UpdateFlashcardRequest updateFlashcardRequest) {
        UUID userId = UUID.fromString(jwt.getSubject());

        FlashcardResponse flashcardResponse = flashcardService.updateFlashcard(userId, deckId, flashcardId, updateFlashcardRequest);

        return ResponseEntity.ok(flashcardResponse);
    }

    @DeleteMapping("/{flashcardId}")
    public ResponseEntity<Void> deleteFlashcard(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID deckId, @PathVariable UUID flashcardId) {
        UUID userId = UUID.fromString(jwt.getSubject());

        flashcardService.deletedFlashcard(userId, deckId, flashcardId);

        return ResponseEntity.noContent().build();
    }
}
