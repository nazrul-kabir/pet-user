package com.example.petimage.dto;

import java.util.List;

public record PetImageResponse(
    List<String> message,  // array of image URLs
    String status
) {}