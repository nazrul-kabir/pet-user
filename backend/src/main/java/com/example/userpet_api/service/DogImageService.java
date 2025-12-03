package com.example.userpet_api.service;

import java.util.List;

public interface DogImageService {
    List<String> fetchRandomDogImages(int count);
}
