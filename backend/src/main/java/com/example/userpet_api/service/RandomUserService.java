package com.example.userpet_api.service;

import com.example.userpet_api.model.UserWithPet;
import java.util.List;

public interface RandomUserService {
    // Fetch random users without nationality filter
    List<UserWithPet> fetchRandomUsers(int count);

    // Fetch random users with optional nationality filter
    // @param count Number of users to fetch
    // @param nationality Nationality code (e.g., "FI", "US", "GB") or null for no filter
    List<UserWithPet> fetchRandomUsers(int count, String nationality);
}
