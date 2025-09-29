package com.chatflow.chatflow.controller;

import com.chatflow.chatflow.model.User;
import com.chatflow.chatflow.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // 👇 NEW: autocomplete endpoint
    @GetMapping("/search")
    public List<String> searchUsers(@RequestParam String q) {
        return userRepo.findByUsernameContainingIgnoreCase(q)
                .stream()
                .map(User::getUsername)
                .toList();
    }
}
