package com.example.userpet_api.service;

import com.example.userpet_api.config.Constants;
import com.example.userpet_api.model.UserWithPet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class RandomUserServiceImpl implements RandomUserService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public RandomUserServiceImpl() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<UserWithPet> fetchRandomUsers(int count) {
        log.debug("Fetching {} random users", count);
        
        // Validate count parameter
        if (count < Constants.MIN_USER_COUNT || count > Constants.MAX_USER_COUNT) {
            log.warn("Invalid user count: {}. Must be between {} and {}", 
                    count, Constants.MIN_USER_COUNT, Constants.MAX_USER_COUNT);
            count = Constants.DEFAULT_USER_COUNT;
        }
        
        try {
            String url = String.format(Constants.API_URL_RANDOM_USER, count);
            String response = restTemplate.getForObject(url, String.class);
            
            if (response == null) {
                // TODO: Handle null response from RandomUser API
                log.warn("Received null response from RandomUser API");
                return List.of();
            }
            
            JsonNode root = objectMapper.readTree(response);
            JsonNode results = root.path("results");
            
            return StreamSupport.stream(results.spliterator(), false)
                .map(this::mapJsonNodeToUser)
                .filter(Objects::nonNull)
                .filter(this::isValidUser)
                .collect(Collectors.toList());
                
        } catch (RestClientException e) {
            log.error("Network error fetching users from RandomUser API", e);
        } catch (Exception e) {
            log.error("Unexpected error processing RandomUser API response", e);
        }
        
        return List.of();
    }

    private UserWithPet mapJsonNodeToUser(JsonNode node) {
        try {
            return UserWithPet.builder()
                .id(extractId(node))
                .gender(node.path("gender").asText())
                .country(node.path("nat").asText())
                .name(buildFullName(node))
                .email(node.path("email").asText())
                .dob(UserWithPet.Dob.builder()
                    .date(node.path("dob").path("date").asText())
                    .age(node.path("dob").path("age").asInt())
                    .build())
                .phone(node.path("phone").asText())
                .build();
        } catch (Exception e) {
            log.warn("Failed to map JSON node to UserWithPet: {}", e.getMessage());
            return null;
        }
    }

    private String extractId(JsonNode node) {
        String idValue = node.path("id").path("value").asText();
        return idValue.isEmpty() ? null : idValue;
    }

    private String buildFullName(JsonNode node) {
        String firstName = node.path("name").path("first").asText();
        String lastName = node.path("name").path("last").asText();
        return firstName + " " + lastName;
    }

    private boolean isValidUser(UserWithPet user) {
        return user.getId() != null && 
               !user.getId().trim().isEmpty() &&
               user.getName() != null && 
               !user.getName().trim().isEmpty() &&
               user.getEmail() != null && 
               user.getEmail().contains("@");
    }
}
