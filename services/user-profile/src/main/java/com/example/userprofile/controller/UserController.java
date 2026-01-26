package com.example.userprofile.controller;

import com.example.userprofile.dto.UserDto;
import com.example.userprofile.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(
            @RequestParam(defaultValue = "10") int results,
            @RequestParam(required = false) String nat) {
        return ResponseEntity.ok(userService.getUsers(results, nat));
    }
}