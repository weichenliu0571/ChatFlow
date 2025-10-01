package com.chatflow.chatflow.service;

import com.chatflow.chatflow.model.Friendship;
import com.chatflow.chatflow.model.User;
import com.chatflow.chatflow.repository.FriendshipRepository;
import com.chatflow.chatflow.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    public FriendshipService(FriendshipRepository friendshipRepository,
            UserRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    // Get all friends of a user
    public List<Friendship> getFriends(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        return friendshipRepository.findByUser(user);
    }

    // Check if two users are friends
    public boolean areFriends(String username, String friendUsername) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        User friend = userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + friendUsername));

        return friendshipRepository.existsByUserAndFriend(user, friend);
    }

    // Remove a friendship (both sides)
    public void removeFriend(String username, String friendUsername) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        User friend = userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + friendUsername));

        friendshipRepository.findByUserAndFriend(user, friend)
                .ifPresent(friendshipRepository::delete);
        friendshipRepository.findByUserAndFriend(friend, user)
                .ifPresent(friendshipRepository::delete);
    }
}
