package com.example.userpet_api.service;

import com.example.userpet_api.config.Constants;
import com.example.userpet_api.model.UserWithPet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserWithPetAggregatorService {
    private final RandomUserService randomUserService;
    private final DogImageService dogImageService;

    public List<UserWithPet> getUsersWithPets(int count) {
        // Delegate to overloaded method with no nationality filter
        return getUsersWithPets(count, null);
    }

    public List<UserWithPet> getUsersWithPets(int count, String nationality) {
        log.debug("Aggregating {} users with pet images (nationality: {})", count,
                nationality != null ? nationality : "all");

        // Validate and normalize count
        count = normalizeCount(count);

        try {
            List<UserWithPet> users = randomUserService.fetchRandomUsers(count, nationality);
            List<String> images = dogImageService.fetchRandomDogImages(count);

            log.debug("Retrieved {} users and {} pet images", users.size(), images.size());

            return aggregateUsersWithImages(users, images);

        } catch (Exception e) {
            log.error("Error during user-pet aggregation", e);
            throw new RuntimeException("Failed to aggregate users with pet images", e);
        }
    }

    private int normalizeCount(int count) {
        if (count < Constants.MIN_USER_COUNT) {
            log.warn("Count {} is below minimum {}. Using default {}.", 
                    count, Constants.MIN_USER_COUNT, Constants.DEFAULT_USER_COUNT);
            return Constants.DEFAULT_USER_COUNT;
        }
        if (count > Constants.MAX_USER_COUNT) {
            log.warn("Count {} exceeds maximum {}. Limiting to {}.", 
                    count, Constants.MAX_USER_COUNT, Constants.MAX_USER_COUNT);
            return Constants.MAX_USER_COUNT;
        }
        return count;
    }

    private List<UserWithPet> aggregateUsersWithImages(List<UserWithPet> users, List<String> images) {
        return IntStream.range(0, users.size())
            .filter(i -> i < images.size()) // Only include users with corresponding images
            .mapToObj(i -> enhanceUserWithImage(users.get(i), images.get(i)))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private UserWithPet enhanceUserWithImage(UserWithPet user, String imageUrl) {
        try {
            // Create a new user with pet image instead of modifying the original one
            return UserWithPet.builder()
                .id(user.getId())
                .gender(user.getGender())
                .country(user.getCountry())
                .name(user.getName())
                .email(user.getEmail())
                .dob(user.getDob())
                .phone(user.getPhone())
                .petImage(imageUrl)
                .build();
        } catch (Exception e) {
            log.warn("Failed to enhance user {} with image: {}", user.getId(), e.getMessage());
            return null;
        }
    }
}
