package org.practice.languageflashcardsapi.user.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateProfileRequest {
    @NotBlank
    private String displayName;

    public CreateProfileRequest() {}

    public CreateProfileRequest(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
