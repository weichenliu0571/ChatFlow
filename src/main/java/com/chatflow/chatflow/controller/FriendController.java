package com.chatflow.chatflow.controller;

import com.chatflow.chatflow.model.Friendship;
import com.chatflow.chatflow.repository.FriendshipRepository;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/friends")
public class FriendController {

    private final FriendshipRepository repo;

    public FriendController(FriendshipRepository repo) {
        this.repo = repo;
    }

    // ➕ Add a friend (no approval needed)
    @PostMapping("/add")
    public String addFriend(@RequestParam String username, Principal principal) {
        Friendship f = new Friendship(principal.getName(), username);
        repo.save(f);
        return "Added " + username + " as a friend!";
    }

    // 📜 Get current user's friends
    @GetMapping
    public List<String> getFriends(Principal principal) {
        return repo.findByRequester(principal.getName())
                .stream()
                .map(Friendship::getAddressee)
                .collect(Collectors.toList());
    }
}
