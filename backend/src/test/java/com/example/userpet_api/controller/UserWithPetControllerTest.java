package com.example.userpet_api.controller;

import com.example.userpet_api.model.UserWithPet;
import com.example.userpet_api.service.UserWithPetAggregatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserWithPetController.class)
class UserWithPetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserWithPetAggregatorService aggregatorService;

    private List<UserWithPet> mockUsers;

    @BeforeEach
    void setUp() {
        mockUsers = Arrays.asList(
            createMockUser("1", "John Doe", "john@example.com", "US", "https://dog1.jpg"),
            createMockUser("2", "Jane Smith", "jane@example.com", "FI", "https://dog2.jpg"),
            createMockUser("3", "Bob Johnson", "bob@example.com", "GB", "https://dog3.jpg")
        );
    }

    @Test
    void testGetUsersWithPet_NoParameters_ShouldReturnDefaultUsers() throws Exception {
        // Given
        when(aggregatorService.getUsersWithPets(50, null)).thenReturn(mockUsers);

        // When & Then
        mockMvc.perform(get("/api/users-with-pet"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].id", is("1")))
            .andExpect(jsonPath("$[0].name", is("John Doe")))
            .andExpect(jsonPath("$[0].email", is("john@example.com")))
            .andExpect(jsonPath("$[0].country", is("US")))
            .andExpect(jsonPath("$[0].petImage", is("https://dog1.jpg")));

        verify(aggregatorService, times(1)).getUsersWithPets(50, null);
    }

    @Test
    void testGetUsersWithPet_WithResultsParameter_ShouldReturnSpecifiedCount() throws Exception {
        // Given
        int count = 20;
        when(aggregatorService.getUsersWithPets(count, null)).thenReturn(mockUsers);

        // When & Then
        mockMvc.perform(get("/api/users-with-pet")
                .param("results", String.valueOf(count)))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", hasSize(3)));

        verify(aggregatorService, times(1)).getUsersWithPets(count, null);
    }

    @Test
    void testGetUsersWithPet_WithNationalityParameter_ShouldFilterByNationality() throws Exception {
        // Given
        String nationality = "FI";
        List<UserWithPet> finnishUsers = Arrays.asList(
            createMockUser("2", "Jane Smith", "jane@example.com", "FI", "https://dog2.jpg")
        );
        when(aggregatorService.getUsersWithPets(50, nationality)).thenReturn(finnishUsers);

        // When & Then
        mockMvc.perform(get("/api/users-with-pet")
                .param("nat", nationality))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].country", is("FI")));

        verify(aggregatorService, times(1)).getUsersWithPets(50, nationality);
    }

    @Test
    void testGetUsersWithPet_WithBothParameters_ShouldApplyBoth() throws Exception {
        // Given
        int count = 20;
        String nationality = "US";
        List<UserWithPet> americanUsers = Arrays.asList(
            createMockUser("1", "John Doe", "john@example.com", "US", "https://dog1.jpg")
        );
        when(aggregatorService.getUsersWithPets(count, nationality)).thenReturn(americanUsers);

        // When & Then
        mockMvc.perform(get("/api/users-with-pet")
                .param("results", String.valueOf(count))
                .param("nat", nationality))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].country", is("US")));

        verify(aggregatorService, times(1)).getUsersWithPets(count, nationality);
    }

    @Test
    void testGetUsersWithPet_WithResults1_ShouldReturnSingleUser() throws Exception {
        // Given
        int count = 1;
        List<UserWithPet> singleUser = Arrays.asList(mockUsers.get(0));
        when(aggregatorService.getUsersWithPets(count, null)).thenReturn(singleUser);

        // When & Then
        mockMvc.perform(get("/api/users-with-pet")
                .param("results", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));

        verify(aggregatorService, times(1)).getUsersWithPets(1, null);
    }

    @Test
    void testGetUsersWithPet_WithResults50_ShouldWork() throws Exception {
        // Given - 50 is the Dog API limit
        int count = 50;
        when(aggregatorService.getUsersWithPets(count, null)).thenReturn(mockUsers);

        // When & Then
        mockMvc.perform(get("/api/users-with-pet")
                .param("results", "50"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)));

        verify(aggregatorService, times(1)).getUsersWithPets(50, null);
    }

    @Test
    void testGetUsersWithPet_WithResults100_ShouldCallServiceWith100() throws Exception {
        // Given - Even though Dog API limits to 50, controller should pass 100 to service
        int count = 100;
        when(aggregatorService.getUsersWithPets(count, null)).thenReturn(mockUsers);

        // When & Then
        mockMvc.perform(get("/api/users-with-pet")
                .param("results", "100"))
            .andExpect(status().isOk());

        verify(aggregatorService, times(1)).getUsersWithPets(100, null);
    }

    @Test
    void testGetUsersWithPet_VerifyResponseStructure() throws Exception {
        // Given
        when(aggregatorService.getUsersWithPets(50, null)).thenReturn(mockUsers);

        // When & Then - Verify complete response structure
        mockMvc.perform(get("/api/users-with-pet"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].name").exists())
            .andExpect(jsonPath("$[0].email").exists())
            .andExpect(jsonPath("$[0].country").exists())
            .andExpect(jsonPath("$[0].gender").exists())
            .andExpect(jsonPath("$[0].phone").exists())
            .andExpect(jsonPath("$[0].dob").exists())
            .andExpect(jsonPath("$[0].dob.date").exists())
            .andExpect(jsonPath("$[0].dob.age").exists())
            .andExpect(jsonPath("$[0].petImage").exists());
    }

    @Test
    void testGetUsersWithPet_WhenServiceReturnsEmptyList_ShouldReturnEmptyArray() throws Exception {
        // Given
        when(aggregatorService.getUsersWithPets(50, null)).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/api/users-with-pet"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testGetUsersWithPet_VerifyCORSEnabled() throws Exception {
        // Given
        when(aggregatorService.getUsersWithPets(50, null)).thenReturn(mockUsers);

        // When & Then - Verify CORS headers are present
        mockMvc.perform(get("/api/users-with-pet")
                .header("Origin", "http://localhost:3000"))
            .andExpect(status().isOk())
            .andExpect(header().exists("Access-Control-Allow-Origin"));
    }

    @Test
    void testGetUsersWithPet_WithDifferentNationalities_ShouldWork() throws Exception {
        // Test multiple nationalities
        String[] nationalities = {"US", "FI", "GB", "DE", "FR"};

        for (String nat : nationalities) {
            when(aggregatorService.getUsersWithPets(50, nat)).thenReturn(mockUsers);

            mockMvc.perform(get("/api/users-with-pet")
                    .param("nat", nat))
                .andExpect(status().isOk());

            verify(aggregatorService, times(1)).getUsersWithPets(50, nat);
        }
    }

    // Helper method to create mock users
    private UserWithPet createMockUser(String id, String name, String email, String country, String petImage) {
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
            .petImage(petImage)
            .build();
    }
}
