package org.practice.languageflashcardsapi.user.exception;

import org.practice.languageflashcardsapi.exception.ResourceAlreadyExistsException;

public class ProfileAlreadyExistsException extends ResourceAlreadyExistsException {
    public ProfileAlreadyExistsException() {
        super("Profile already exists.");
    }
}
