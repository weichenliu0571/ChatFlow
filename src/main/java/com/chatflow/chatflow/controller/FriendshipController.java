package com.chatflow.chatflow.controller;

import com.chatflow.chatflow.model.Friendship;
import com.chatflow.chatflow.service.FriendshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friendships")
public class FriendshipController {

    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    // Get all friends of a user
    @GetMapping("/{username}")
    public ResponseEntity<List<Friendship>> getFriends(@PathVariable String username) {
        return ResponseEntity.ok(friendshipService.getFriends(username));
    }

    // Check if two users are friends
    @GetMapping("/{user}/{friend}")
    public ResponseEntity<Boolean> areFriends(
            @PathVariable String user,
            @PathVariable String friend) {
        return ResponseEntity.ok(friendshipService.areFriends(user, friend));
    }

    // Remove a friendship (both sides)
    @DeleteMapping("/{user}/{friend}")
    public ResponseEntity<Void> removeFriend(
            @PathVariable String user,
            @PathVariable String friend) {
        friendshipService.removeFriend(user, friend);
        return ResponseEntity.noContent().build();
    }
}
