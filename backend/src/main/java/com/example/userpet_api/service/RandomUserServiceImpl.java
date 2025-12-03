package com.example.userpet_api.service;

import com.example.userpet_api.model.UserWithPet;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

@Service
public class RandomUserServiceImpl implements RandomUserService {
    private static final String API_URL = "https://randomuser.me/api/?results=%d";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<UserWithPet> fetchRandomUsers(int count) {
        List<UserWithPet> users = new ArrayList<>();
        try {
            String url = String.format(API_URL, count);
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode results = root.path("results");
            for (JsonNode node : results) {
                UserWithPet user = new UserWithPet();
                user.setId(node.path("id").path("value").asText("").isEmpty() ? null : node.path("id").path("value").asText());
                user.setGender(node.path("gender").asText());
                user.setCountry(node.path("nat").asText());
                user.setName(node.path("name").path("first").asText() + " " + node.path("name").path("last").asText());
                user.setEmail(node.path("email").asText());
                String dobDate = node.path("dob").path("date").asText();
                int dobAge = node.path("dob").path("age").asInt();
                user.setDob(new UserWithPet.Dob(dobDate, dobAge));
                user.setPhone(node.path("phone").asText());
                users.add(user);
            }
        } catch (RestClientException | NullPointerException | IllegalArgumentException e) {
            // Log error and return empty list or partial results
        } catch (Exception e) {
            // Log error
        }
        return users;
    }
}
