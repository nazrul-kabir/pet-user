package com.example.userprofile.dto;

public record UserDto(
    String uuid,
    String firstName,
    String lastName,
    String email,
    String pictureLarge,
    String nationality
) {}