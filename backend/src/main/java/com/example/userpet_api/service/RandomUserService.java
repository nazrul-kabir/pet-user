package com.example.userpet_api.service;

import com.example.userpet_api.model.UserWithPet;
import java.util.List;

public interface RandomUserService {
    List<UserWithPet> fetchRandomUsers(int count);
}
