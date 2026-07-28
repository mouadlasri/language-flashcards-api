package org.practice.languageflashcardsapi.deck;

import jakarta.validation.Valid;
import org.practice.languageflashcardsapi.deck.dto.CreateDeckRequest;
import org.practice.languageflashcardsapi.deck.dto.DeckResponse;
import org.practice.languageflashcardsapi.deck.dto.UpdateDeckRequest;
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
@RequestMapping("/api/v1/decks")
public class DeckController {
    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @GetMapping
    public ResponseEntity<Page<DeckResponse>> getAllDecks(@AuthenticationPrincipal Jwt jwt, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        UUID userId = UUID.fromString(jwt.getSubject());

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<DeckResponse> deckResponsePage = deckService.getAllDecks(userId, pageable);

        return ResponseEntity.ok(deckResponsePage);
    }

    @GetMapping("/{deckId}")
    public ResponseEntity<DeckResponse> getDeckById(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID deckId) {
        UUID userId = UUID.fromString(jwt.getSubject());

        DeckResponse deckResponse = deckService.getDeckByIdAndUserId(deckId, userId);

        return ResponseEntity.ok(deckResponse);
    }

    @PostMapping
    public ResponseEntity<DeckResponse> createDeck(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CreateDeckRequest createDeckRequest) {
        UUID userId = UUID.fromString(jwt.getSubject());

        DeckResponse deckResponse = deckService.createDeck(userId, createDeckRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(deckResponse);
    }

    @PatchMapping("/{deckId}")
    public ResponseEntity<DeckResponse> updateDeck(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID deckId, @Valid @RequestBody UpdateDeckRequest updateDeckRequest) {
        UUID userId = UUID.fromString(jwt.getSubject());

        DeckResponse deckResponse = deckService.updateDeck(userId, deckId, updateDeckRequest);

        return ResponseEntity.ok(deckResponse);
    }

    @DeleteMapping("/{deckId}")
    public ResponseEntity<Void> deleteDeck(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID deckId) {
        UUID userId = UUID.fromString(jwt.getSubject());

        deckService.deleteDeck(userId, deckId);

        return ResponseEntity.noContent().build();
    }
}
