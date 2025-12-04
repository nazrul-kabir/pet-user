package com.example.userpet_api.service;

import com.example.userpet_api.model.UserWithPet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserWithPetAggregatorServiceTest {

    @Mock
    private RandomUserService randomUserService;

    @Mock
    private DogImageService dogImageService;

    @InjectMocks
    private UserWithPetAggregatorService aggregatorService;

    private List<UserWithPet> mockUsers;
    private List<String> mockImages;

    @BeforeEach
    void setUp() {
        // Create mock users
        mockUsers = Arrays.asList(
            createMockUser("1", "John Doe", "john@example.com", "US"),
            createMockUser("2", "Jane Smith", "jane@example.com", "FI"),
            createMockUser("3", "Bob Johnson", "bob@example.com", "GB")
        );

        // Create mock images
        mockImages = Arrays.asList(
            "https://images.dog.ceo/breeds/hound-afghan/1.jpg",
            "https://images.dog.ceo/breeds/terrier-australian/2.jpg",
            "https://images.dog.ceo/breeds/bulldog-french/3.jpg"
        );
    }

    @Test
    void testGetUsersWithPets_ShouldAggregateSuccessfully() {
        // Given
        int count = 3;
        when(randomUserService.fetchRandomUsers(count, null)).thenReturn(mockUsers);
        when(dogImageService.fetchRandomDogImages(count)).thenReturn(mockImages);

        // When
        List<UserWithPet> result = aggregatorService.getUsersWithPets(count);

        // Then
        assertNotNull(result, "Result should not be null");
        assertEquals(3, result.size(), "Should return 3 users with pets");

        // Verify each user has a pet image
        for (int i = 0; i < result.size(); i++) {
            assertNotNull(result.get(i).getPetImage(), "User should have a pet image");
            assertEquals(mockImages.get(i), result.get(i).getPetImage(), "Pet image should match");
        }

        // Verify services were called correctly
        verify(randomUserService, times(1)).fetchRandomUsers(count, null);
        verify(dogImageService, times(1)).fetchRandomDogImages(count);
    }

    @Test
    void testGetUsersWithPets_WithNationality_ShouldPassNationalityFilter() {
        // Given
        int count = 3;
        String nationality = "FI";
        when(randomUserService.fetchRandomUsers(count, nationality)).thenReturn(mockUsers);
        when(dogImageService.fetchRandomDogImages(count)).thenReturn(mockImages);

        // When
        List<UserWithPet> result = aggregatorService.getUsersWithPets(count, nationality);

        // Then
        assertNotNull(result, "Result should not be null");
        assertEquals(3, result.size(), "Should return 3 users with pets");

        // Verify nationality filter was passed to service
        verify(randomUserService, times(1)).fetchRandomUsers(count, nationality);
        verify(dogImageService, times(1)).fetchRandomDogImages(count);
    }

    @Test
    void testGetUsersWithPets_WhenMoreUsersThanImages_ShouldReturnOnlyMatchedPairs() {
        // Given - More users than images
        int count = 3;
        List<String> twoImages = Arrays.asList(mockImages.get(0), mockImages.get(1));
        when(randomUserService.fetchRandomUsers(count, null)).thenReturn(mockUsers);
        when(dogImageService.fetchRandomDogImages(count)).thenReturn(twoImages);

        // When
        List<UserWithPet> result = aggregatorService.getUsersWithPets(count);

        // Then - Should only return users that have matching images
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Should return only 2 users (matching available images)");
    }

    @Test
    void testGetUsersWithPets_WhenMoreImagesThanUsers_ShouldReturnAllUsers() {
        // Given - More images than users
        int count = 3;
        List<UserWithPet> twoUsers = Arrays.asList(mockUsers.get(0), mockUsers.get(1));
        when(randomUserService.fetchRandomUsers(count, null)).thenReturn(twoUsers);
        when(dogImageService.fetchRandomDogImages(count)).thenReturn(mockImages);

        // When
        List<UserWithPet> result = aggregatorService.getUsersWithPets(count);

        // Then
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Should return all 2 users");
    }

    @Test
    void testGetUsersWithPets_WithCountBelowMinimum_ShouldNormalizeToDefault() {
        // Given
        int invalidCount = 0;
        int defaultCount = 50; // Default from Constants
        when(randomUserService.fetchRandomUsers(defaultCount, null)).thenReturn(mockUsers);
        when(dogImageService.fetchRandomDogImages(defaultCount)).thenReturn(mockImages);

        // When
        List<UserWithPet> result = aggregatorService.getUsersWithPets(invalidCount);

        // Then
        assertNotNull(result, "Result should not be null");
        verify(randomUserService, times(1)).fetchRandomUsers(defaultCount, null);
        verify(dogImageService, times(1)).fetchRandomDogImages(defaultCount);
    }

    @Test
    void testGetUsersWithPets_WithCountAboveMaximum_ShouldNormalizeToMax() {
        // Given
        int invalidCount = 10000;
        int maxCount = 1000; // Max from Constants
        when(randomUserService.fetchRandomUsers(maxCount, null)).thenReturn(mockUsers);
        when(dogImageService.fetchRandomDogImages(maxCount)).thenReturn(mockImages);

        // When
        List<UserWithPet> result = aggregatorService.getUsersWithPets(invalidCount);

        // Then
        assertNotNull(result, "Result should not be null");
        verify(randomUserService, times(1)).fetchRandomUsers(maxCount, null);
        verify(dogImageService, times(1)).fetchRandomDogImages(maxCount);
    }

    @Test
    void testGetUsersWithPets_WhenServicesReturnEmptyLists_ShouldReturnEmptyList() {
        // Given
        int count = 3;
        when(randomUserService.fetchRandomUsers(count, null)).thenReturn(List.of());
        when(dogImageService.fetchRandomDogImages(count)).thenReturn(List.of());

        // When
        List<UserWithPet> result = aggregatorService.getUsersWithPets(count);

        // Then
        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result should be empty when services return empty lists");
    }

    @Test
    void testGetUsersWithPets_ShouldPreserveUserData() {
        // Given
        int count = 3;
        when(randomUserService.fetchRandomUsers(count, null)).thenReturn(mockUsers);
        when(dogImageService.fetchRandomDogImages(count)).thenReturn(mockImages);

        // When
        List<UserWithPet> result = aggregatorService.getUsersWithPets(count);

        // Then
        assertNotNull(result, "Result should not be null");

        for (int i = 0; i < result.size(); i++) {
            // Verify all original user data is preserved
            assertEquals(mockUsers.get(i).getId(), result.get(i).getId(), "User ID should be preserved");
            assertEquals(mockUsers.get(i).getName(), result.get(i).getName(), "User name should be preserved");
            assertEquals(mockUsers.get(i).getEmail(), result.get(i).getEmail(), "User email should be preserved");
            assertEquals(mockUsers.get(i).getCountry(), result.get(i).getCountry(), "User country should be preserved");
            assertEquals(mockUsers.get(i).getGender(), result.get(i).getGender(), "User gender should be preserved");
            assertEquals(mockUsers.get(i).getPhone(), result.get(i).getPhone(), "User phone should be preserved");
            assertEquals(mockUsers.get(i).getDob(), result.get(i).getDob(), "User DOB should be preserved");

            // And pet image is added
            assertEquals(mockImages.get(i), result.get(i).getPetImage(), "Pet image should be added");
        }
    }

    @Test
    void testGetUsersWithPets_WithValidCount_ShouldWork() {
        // Given
        int count = 50; // Valid count within range
        when(randomUserService.fetchRandomUsers(count, null)).thenReturn(mockUsers);
        when(dogImageService.fetchRandomDogImages(count)).thenReturn(mockImages);

        // When
        List<UserWithPet> result = aggregatorService.getUsersWithPets(count);

        // Then
        assertNotNull(result, "Result should not be null");
        verify(randomUserService, times(1)).fetchRandomUsers(count, null);
        verify(dogImageService, times(1)).fetchRandomDogImages(count);
    }

    // Helper method to create mock users
    private UserWithPet createMockUser(String id, String name, String email, String country) {
        return UserWithPet.builder()
            .id(id)
            .name(name)
            .email(email)
            .country(country)
            .gender("male")
            .phone("123-456-7890")
            .dob(UserWithPet.Dob.builder()
                .date("1990-01-01T00:00:00.000Z")
                .age(34)
                .build())
            .build();
    }
}
