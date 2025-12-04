package com.example.userpet_api.controller;

import com.example.userpet_api.config.Constants;
import com.example.userpet_api.model.UserWithPet;
import com.example.userpet_api.service.UserWithPetAggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserWithPetController {
    @Autowired
    private UserWithPetAggregatorService aggregatorService;

    @GetMapping("/users-with-pet")
    public List<UserWithPet> getUsersWithPet(
            @RequestParam(defaultValue = "" + Constants.DEFAULT_USER_COUNT) int results,
            @RequestParam(required = false) String nat) {
        // TODO: Add pagination options
        // nationality filtering via 'nat' query parameter
        // Example: /api/users-with-pet?results=20&nat=FI
        return aggregatorService.getUsersWithPets(results, nat);
    }
}
