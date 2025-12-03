package com.example.userpet_api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

@Service
public class DogImageServiceImpl implements DogImageService {
    private static final String API_URL = "https://dog.ceo/api/breeds/image/random/%d";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<String> fetchRandomDogImages(int count) {
        List<String> images = new ArrayList<>();
        try {
            String url = String.format(API_URL, count);
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode message = root.path("message");
            if (message.isArray()) {
                for (JsonNode img : message) {
                    images.add(img.asText());
                }
            } else if (message.isTextual()) {
                images.add(message.asText());
            }
        } catch (RestClientException | NullPointerException | IllegalArgumentException e) {
            // Log error and return empty list or partial results
        } catch (Exception e) {
            // Log error
        }
        return images;
    }
}
