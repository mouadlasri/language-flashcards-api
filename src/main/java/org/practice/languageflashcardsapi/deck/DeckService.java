package org.practice.languageflashcardsapi.deck;

import org.practice.languageflashcardsapi.deck.dto.CreateDeckRequest;
import org.practice.languageflashcardsapi.deck.dto.DeckResponse;
import org.practice.languageflashcardsapi.deck.dto.UpdateDeckRequest;
import org.practice.languageflashcardsapi.deck.exception.DeckNotFoundException;
import org.practice.languageflashcardsapi.exception.InvalidRequestException;
import org.practice.languageflashcardsapi.user.User;
import org.practice.languageflashcardsapi.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

@Service
public class DeckService {
    private final DeckRepository deckRepository;
    private final UserService userService;

    public DeckService(DeckRepository deckRepository, UserService userService) {
        this.deckRepository = deckRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public Page<DeckResponse> getAllDecks(UUID userId, Pageable pageable) {
        userService.getActiveUserEntityById(userId);
        Page<Deck> deckPage = deckRepository.findAllByUserIdAndDeletedAtIsNull(userId, pageable);

        Page<DeckResponse> deckResponsePage = deckPage.map(deck -> toDeckResponse(deck));

        return deckResponsePage;
    }

    @Transactional(readOnly = true)
    public DeckResponse getDeckById(UUID userId, UUID deckId) {
        userService.getActiveUserEntityById(userId);

        Deck deck = deckRepository.findByIdAndUserIdAndDeletedAtIsNull(deckId, userId)
                .orElseThrow(() -> new DeckNotFoundException());

        return toDeckResponse(deck);
    }

    @Transactional
    public DeckResponse createDeck(UUID userId, CreateDeckRequest createDeckRequest) {
        User user = userService.getActiveUserEntityById(userId);
        String title = createDeckRequest.getTitle();
        String description = createDeckRequest.getDescription();
        Deck deck = new Deck(user, title, description);

        Deck newDeck = deckRepository.save(deck);

        return toDeckResponse(newDeck);
    }

    @Transactional
    public DeckResponse updateDeck(UUID userId, UUID deckId, UpdateDeckRequest updateDeckRequest) {
        userService.getActiveUserEntityById(userId);

        Deck deck = deckRepository.findByIdAndUserIdAndDeletedAtIsNull(deckId, userId)
                .orElseThrow(() -> new DeckNotFoundException());

        String title = updateDeckRequest.getTitle();
        String description = updateDeckRequest.getDescription();

        if (title != null) {
            if (title.isBlank()) {
                throw new InvalidRequestException("Title must not be blank.");
            }

            if (!deck.getTitle().equals(title)) {
                deck.setTitle(title);
            }
        }

        if (description != null
                && !Objects.equals(deck.getDescription(), description)) {
            deck.setDescription(description);
        }


        return toDeckResponse(deck);
    }

    @Transactional
    public void deleteDeck(UUID userId, UUID deckId) {
        userService.getActiveUserEntityById(userId);

        Deck deck = deckRepository.findByIdAndUserIdAndDeletedAtIsNull(deckId, userId)
                .orElseThrow(() -> new DeckNotFoundException());

        deck.setDeletedAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    private DeckResponse toDeckResponse(Deck deck) {
        return new DeckResponse(
                deck.getId(),
                deck.getTitle(),
                deck.getDescription(),
                deck.getCreatedAt(),
                deck.getUpdatedAt()
        );
    }
}
