package org.practice.languageflashcardsapi.deck.exception;

import org.practice.languageflashcardsapi.exception.ResourceNotFoundException;

public class DeckNotFoundException extends ResourceNotFoundException {
    public DeckNotFoundException() {
        super("Deck not found.");
    }
}
