package com.example.userpet_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWithPet {
    private String id;
    private String gender;
    private String country;
    private String name;
    private String email;
    private Dob dob;
    private String phone;
    private String petImage;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Dob {
        private String date;
        private int age;
    }
}
