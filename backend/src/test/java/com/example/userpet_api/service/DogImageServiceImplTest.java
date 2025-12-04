package com.example.userpet_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DogImageServiceImplTest {

    private DogImageService dogImageService;

    @BeforeEach
    void setUp() {
        dogImageService = new DogImageServiceImpl();
    }

    @Test
    void testFetchRandomDogImages_WithValidCount_ShouldReturnImages() {
        // Given
        int count = 5;

        // When
        List<String> images = dogImageService.fetchRandomDogImages(count);

        // Then
        assertNotNull(images, "Images list should not be null");
        assertEquals(count, images.size(), "Should return exactly " + count + " images");

        // Verify all images are valid URLs
        for (String image : images) {
            assertNotNull(image, "Image URL should not be null");
            assertTrue(image.startsWith("https://"), "Image should be a valid HTTPS URL");
            assertTrue(image.contains("dog.ceo"), "Image should be from dog.ceo domain");
        }
    }

    @Test
    void testFetchRandomDogImages_WithCount1_ShouldReturnSingleImage() {
        // Given
        int count = 1;

        // When
        List<String> images = dogImageService.fetchRandomDogImages(count);

        // Then
        assertNotNull(images, "Images list should not be null");
        assertEquals(1, images.size(), "Should return exactly 1 image");
    }

    @Test
    void testFetchRandomDogImages_WithCount50_ShouldReturnMaxImages() {
        // Given - Dog API limit is 50
        int count = 50;

        // When
        List<String> images = dogImageService.fetchRandomDogImages(count);

        // Then
        assertNotNull(images, "Images list should not be null");
        assertEquals(50, images.size(), "Should return exactly 50 images (API limit)");
    }

    @Test
    void testFetchRandomDogImages_WithZeroCount_ShouldReturnEmptyOrError() {
        // Given
        int count = 0;

        // When
        List<String> images = dogImageService.fetchRandomDogImages(count);

        // Then - API might return error or empty list
        assertNotNull(images, "Images list should not be null");
    }

    @Test
    void testFetchRandomDogImages_MultipleCalls_ShouldReturnDifferentImages() {
        // Given
        int count = 3;

        // When
        List<String> images1 = dogImageService.fetchRandomDogImages(count);
        List<String> images2 = dogImageService.fetchRandomDogImages(count);

        // Then - Different calls should return different random images
        assertNotNull(images1, "First images list should not be null");
        assertNotNull(images2, "Second images list should not be null");
        assertEquals(count, images1.size(), "First call should return " + count + " images");
        assertEquals(count, images2.size(), "Second call should return " + count + " images");

        // At least some images should be different (randomness check)
        boolean hasDifferentImages = false;
        for (int i = 0; i < count; i++) {
            if (!images1.get(i).equals(images2.get(i))) {
                hasDifferentImages = true;
                break;
            }
        }
        assertTrue(hasDifferentImages, "Multiple calls should return different random images");
    }
}
