package com.example.userprofile.service;

import com.example.userprofile.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class UserService {

    private final WebClient webClient;
    private static final String RANDOM_USER_URL = "https://randomuser.me/api/";
    private static final String FIXED_SEED = "aimopark2025";

    public UserService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(RANDOM_USER_URL).build();
    }

    public List<UserDto> getUsers(int results, String nat) {
        // Validate params (boring but production-grade)
        final int finalResults = (results < 1 || results > 100) ? 10 : results;  // cap low for free-tier
        final String finalNat = (nat == null || nat.isBlank()) ? "fi" : nat;

        var response = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .queryParam("results", finalResults)
                .queryParam("nat", finalNat)
                .queryParam("seed", FIXED_SEED)
                .build())
            .retrieve()
            .bodyToMono(RandomUserResponse.class)
            .block();  // sync for simplicity; use Mono/Flux later

        return response.results().stream()
            .map(u -> new UserDto(
                u.login().uuid(),
                u.name().first(),
                u.name().last(),
                u.email(),
                u.picture().large(),
                u.nat()
            ))
            .toList();
    }

    // Record for response mapping (add @JsonIgnoreProperties if needed)
    private record RandomUserResponse(List<UserRaw> results, Info info) {}
    private record UserRaw(Name name, String email, Picture picture, Login login, String nat) {}
    private record Name(String first, String last) {}
    private record Picture(String large) {}
    private record Login(String uuid) {}
    private record Info(String seed) {}
}