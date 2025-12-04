package com.example.userpet_api.service;

import com.example.userpet_api.model.UserWithPet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RandomUserServiceImplTest {

    private RandomUserService randomUserService;

    @BeforeEach
    void setUp() {
        randomUserService = new RandomUserServiceImpl();
    }

    @Test
    void testFetchRandomUsers_WithValidCount_ShouldReturnUsers() {
        // Given
        int count = 10;

        // When
        List<UserWithPet> users = randomUserService.fetchRandomUsers(count);

        // Then
        assertNotNull(users, "Users list should not be null");
        assertTrue(users.size() > 0, "Should return at least some users");
        assertTrue(users.size() <= count, "Should not return more than requested");

        // Verify user data structure
        UserWithPet firstUser = users.get(0);
        assertNotNull(firstUser.getId(), "User should have an ID");
        assertNotNull(firstUser.getName(), "User should have a name");
        assertNotNull(firstUser.getEmail(), "User should have an email");
        assertTrue(firstUser.getEmail().contains("@"), "Email should be valid");
        assertNotNull(firstUser.getCountry(), "User should have a country");
        assertNotNull(firstUser.getGender(), "User should have a gender");
        assertNotNull(firstUser.getDob(), "User should have date of birth");
        assertNotNull(firstUser.getPhone(), "User should have a phone number");
    }

    @Test
    void testFetchRandomUsers_WithNationality_ShouldReturnFilteredUsers() {
        // Given
        int count = 10;
        String nationality = "FI";

        // When
        List<UserWithPet> users = randomUserService.fetchRandomUsers(count, nationality);

        // Then
        assertNotNull(users, "Users list should not be null");
        assertTrue(users.size() > 0, "Should return at least some users");

        // Verify all users are from the specified nationality
        for (UserWithPet user : users) {
            assertEquals(nationality, user.getCountry(), "All users should be from " + nationality);
        }
    }

    @Test
    void testFetchRandomUsers_WithFixedSeed_ShouldReturnConsistentResults() {
        // Given
        int count = 5;

        // When - Make two calls
        List<UserWithPet> users1 = randomUserService.fetchRandomUsers(count);
        List<UserWithPet> users2 = randomUserService.fetchRandomUsers(count);

        // Then - Results should be identical due to fixed seed
        assertNotNull(users1, "First users list should not be null");
        assertNotNull(users2, "Second users list should not be null");
        assertEquals(users1.size(), users2.size(), "Both calls should return same number of users");

        // Verify users are identical
        for (int i = 0; i < users1.size(); i++) {
            assertEquals(users1.get(i).getId(), users2.get(i).getId(),
                    "Users should be identical due to fixed seed");
            assertEquals(users1.get(i).getName(), users2.get(i).getName(),
                    "User names should be identical due to fixed seed");
            assertEquals(users1.get(i).getEmail(), users2.get(i).getEmail(),
                    "User emails should be identical due to fixed seed");
        }
    }

    @Test
    void testFetchRandomUsers_WithInvalidCountTooLow_ShouldReturnDefaultCount() {
        // Given
        int invalidCount = 0;

        // When
        List<UserWithPet> users = randomUserService.fetchRandomUsers(invalidCount);

        // Then
        assertNotNull(users, "Users list should not be null");
        assertTrue(users.size() > 0, "Should return default number of users");
    }

    @Test
    void testFetchRandomUsers_WithInvalidCountTooHigh_ShouldReturnDefaultCount() {
        // Given
        int invalidCount = 10000; // Above max

        // When
        List<UserWithPet> users = randomUserService.fetchRandomUsers(invalidCount);

        // Then
        assertNotNull(users, "Users list should not be null");
        assertTrue(users.size() > 0, "Should return default number of users");
    }

    @Test
    void testFetchRandomUsers_WithNullNationality_ShouldReturnAllNationalities() {
        // Given
        int count = 10;
        String nationality = null;

        // When
        List<UserWithPet> users = randomUserService.fetchRandomUsers(count, nationality);

        // Then
        assertNotNull(users, "Users list should not be null");
        assertTrue(users.size() > 0, "Should return users");

        // Verify we get users (could be from any country)
        for (UserWithPet user : users) {
            assertNotNull(user.getCountry(), "User should have a country");
            assertFalse(user.getCountry().isEmpty(), "Country should not be empty");
        }
    }

    @Test
    void testFetchRandomUsers_WithEmptyNationality_ShouldReturnAllNationalities() {
        // Given
        int count = 10;
        String nationality = "";

        // When
        List<UserWithPet> users = randomUserService.fetchRandomUsers(count, nationality);

        // Then
        assertNotNull(users, "Users list should not be null");
        assertTrue(users.size() > 0, "Should return users");
    }

    @Test
    void testFetchRandomUsers_WithMultipleNationalities_ShouldWork() {
        // Given
        int count = 5;

        // When - Fetch from different nationalities
        List<UserWithPet> usersUS = randomUserService.fetchRandomUsers(count, "US");
        List<UserWithPet> usersFI = randomUserService.fetchRandomUsers(count, "FI");
        List<UserWithPet> usersGB = randomUserService.fetchRandomUsers(count, "GB");

        // Then
        assertNotNull(usersUS, "US users should not be null");
        assertNotNull(usersFI, "FI users should not be null");
        assertNotNull(usersGB, "GB users should not be null");

        assertTrue(usersUS.size() > 0, "Should return US users");
        assertTrue(usersFI.size() > 0, "Should return FI users");
        assertTrue(usersGB.size() > 0, "Should return GB users");

        // Verify nationality filtering
        usersUS.forEach(user -> assertEquals("US", user.getCountry()));
        usersFI.forEach(user -> assertEquals("FI", user.getCountry()));
        usersGB.forEach(user -> assertEquals("GB", user.getCountry()));
    }

    @Test
    void testFetchRandomUsers_UserValidation_ShouldFilterInvalidUsers() {
        // Given
        int count = 10;

        // When
        List<UserWithPet> users = randomUserService.fetchRandomUsers(count);

        // Then - All returned users should be valid
        assertNotNull(users, "Users list should not be null");

        for (UserWithPet user : users) {
            // Verify all users have required fields (validation passed)
            assertNotNull(user.getId(), "User ID should not be null");
            assertFalse(user.getId().trim().isEmpty(), "User ID should not be empty");
            assertNotNull(user.getName(), "User name should not be null");
            assertFalse(user.getName().trim().isEmpty(), "User name should not be empty");
            assertNotNull(user.getEmail(), "User email should not be null");
            assertTrue(user.getEmail().contains("@"), "Email should contain @");
        }
    }
}
