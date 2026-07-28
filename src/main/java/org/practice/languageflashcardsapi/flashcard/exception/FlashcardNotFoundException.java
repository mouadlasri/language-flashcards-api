package org.practice.languageflashcardsapi.flashcard.exception;

import org.practice.languageflashcardsapi.exception.ResourceNotFoundException;

public class FlashcardNotFoundException extends ResourceNotFoundException {
    public FlashcardNotFoundException() {
        super("Flashcard not found.");
    }
}
