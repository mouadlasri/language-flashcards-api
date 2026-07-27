package org.practice.languageflashcardsapi.user;

import jakarta.validation.Valid;
import org.practice.languageflashcardsapi.user.dto.CreateProfileRequest;
import org.practice.languageflashcardsapi.user.dto.UpdateProfileRequest;
import org.practice.languageflashcardsapi.user.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/me/profile")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());

        UserResponse userResponse = userService.getProfile(userId);

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createProfile(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CreateProfileRequest createProfileRequest) {
        UUID userId = UUID.fromString(jwt.getSubject());
        String email = jwt.getClaimAsString("email");

        UserResponse userResponse = userService.createProfile(userId, email, createProfileRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PatchMapping
    public ResponseEntity<UserResponse> updateProfile(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
        UUID userId = UUID.fromString(jwt.getSubject());

        UserResponse userResponse = userService.updateProfile(userId, updateProfileRequest);

        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProfile(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());

        userService.deleteProfile(userId);

        return ResponseEntity.noContent().build();
    }

}
