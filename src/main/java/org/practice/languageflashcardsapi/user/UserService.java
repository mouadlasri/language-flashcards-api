package org.practice.languageflashcardsapi.user;

import org.practice.languageflashcardsapi.user.dto.CreateProfileRequest;
import org.practice.languageflashcardsapi.user.dto.UpdateProfileRequest;
import org.practice.languageflashcardsapi.user.dto.UserResponse;
import org.practice.languageflashcardsapi.user.exception.ProfileAlreadyExistsException;
import org.practice.languageflashcardsapi.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User getActiveUserEntityById(UUID userId) {
         return userRepository.findUserByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new UserNotFoundException());
    }

    @Transactional(readOnly = true)
    public UserResponse getProfile(UUID userId) {
        User user = userRepository.findUserByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new UserNotFoundException());

        return toUserResponse(user);
    }

    @Transactional
    public UserResponse createProfile(UUID userId, String email, CreateProfileRequest createProfileRequest) {
        if (userRepository.existsById(userId) || userRepository.existsByEmail(email)) {
            throw new ProfileAlreadyExistsException();
        }

        User profile = new User(
                userId,
                createProfileRequest.getDisplayName(),
                email
        );

        User newProfile = userRepository.save(profile);

        return toUserResponse(newProfile);
    }

    @Transactional
    public UserResponse updateProfile(UUID userId, UpdateProfileRequest updateProfileRequest) {
        String displayName = updateProfileRequest.getDisplayName();

        User user = userRepository.findUserByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new UserNotFoundException());

        if (displayName != null && !user.getDisplayName().equals(displayName)) {
            user.setDisplayName(displayName);
        }

        return toUserResponse(user);
    }

    @Transactional
    public void deleteProfile(UUID userId) {
        User user = userRepository.findUserByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new UserNotFoundException());

        user.setDeletedAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getDisplayName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
