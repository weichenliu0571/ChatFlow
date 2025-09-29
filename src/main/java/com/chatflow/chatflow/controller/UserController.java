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

    @GetMapping
    public List<String> listUsers(Principal principal) {
        String currentUser = principal.getName();
        return userRepo.findAll().stream()
                .map(User::getUsername)
                .filter(username -> !username.equals(currentUser)) // exclude yourself
                .collect(Collectors.toList());
    }
}
