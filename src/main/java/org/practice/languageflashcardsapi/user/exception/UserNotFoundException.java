package org.practice.languageflashcardsapi.user.exception;

import org.practice.languageflashcardsapi.exception.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException  {
    public UserNotFoundException() {
        super("User not found.");
    }
}
