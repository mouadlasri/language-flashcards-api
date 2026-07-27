package org.practice.languageflashcardsapi.user.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public class UserResponse {
    private UUID id;
    private String displayName;
    private String email;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public UserResponse(UUID id, String displayName, String email, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
