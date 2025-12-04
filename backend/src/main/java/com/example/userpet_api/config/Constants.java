package com.example.userpet_api.config;

public final class Constants {
    public static final int MIN_USER_COUNT = 1;
    public static final int MAX_USER_COUNT = 1000;
    public static final int DEFAULT_USER_COUNT = 10;
    public static final int MAX_VALID_USER_COUNT = 100;
    
    // Fixed seed for consistent user results across API calls
    public static final String FIXED_SEED = "aimopark2025";

    public static final String API_URL_RANDOM_USER = "https://randomuser.me/api/?results=%d&seed=%s";
    public static final String API_URL_DOG_IMAGE = "https://dog.ceo/api/breeds/image/random/%d";
    
    public static final String ENDPOINT_USERS_WITH_PET = "/api/users-with-pet";
    
    private Constants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }
}