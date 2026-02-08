package com.example.petimage.service;

import com.example.petimage.dto.PetImageResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Service
public class PetService {

    private final WebClient webClient;
    private static final String DOG_CEO_BASE = "https://dog.ceo/api";

    public PetService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(DOG_CEO_BASE).build();
    }

    public List<String> getRandomPetImages(int count) {
        // Pragmatic bounds: API caps at 50 silently
        if (count < 1) count = 1;
        if (count > 50) count = 50;

        PetImageResponse response = webClient.get()
            .uri("/breeds/image/random/{count}", count)
            .retrieve()
            .bodyToMono(PetImageResponse.class)
            .block();  // sync for MVP; later make reactive

        if (response == null || !"success".equals(response.status())) {
            return Collections.emptyList();  // or throw custom exception later
        }

        return response.message() != null ? response.message() : Collections.emptyList();
    }
}