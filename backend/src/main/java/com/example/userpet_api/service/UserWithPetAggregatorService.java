package com.example.userpet_api.service;

import com.example.userpet_api.model.UserWithPet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserWithPetAggregatorService {
    @Autowired
    private RandomUserService randomUserService;
    @Autowired
    private DogImageService dogImageService;

    public List<UserWithPet> getUsersWithPets(int count) {
        List<UserWithPet> users = randomUserService.fetchRandomUsers(count);
        List<String> images = dogImageService.fetchRandomDogImages(count);
        List<UserWithPet> result = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            UserWithPet user = users.get(i);
            if (i < images.size()) {
                user.setPetImage(images.get(i));
            } else {
                user.setPetImage(null);
            }
            // Exclude users with null/empty id or null petImage
            if (user.getId() != null && !user.getId().isEmpty() && user.getPetImage() != null) {
                result.add(user);
            }
        }
        return result;
    }
}
