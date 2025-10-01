package com.chatflow.chatflow.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatflow.chatflow.model.FriendRequest;
import com.chatflow.chatflow.service.FriendRequestService;

@RestController
@RequestMapping("/api/friend-requests")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    // for sending the friend request
    @PostMapping("/{requester}/{addressee}")
    public ResponseEntity<Void> send(@PathVariable String requester,
                                 @PathVariable String addressee) {
    friendRequestService.sendRequest(requester, addressee);
    return ResponseEntity.status(201).build();
}

    // Accept request
    @PostMapping("/{id}/accept")
    public ResponseEntity<Void> acceptRequest(@PathVariable Long id) {
        friendRequestService.acceptRequest(id);
        return ResponseEntity.ok().build();
    }

    // Decline request
    @PostMapping("/{id}/decline")
    public ResponseEntity<Void> declineRequest(@PathVariable Long id) {
        friendRequestService.declineRequest(id);
        return ResponseEntity.ok().build();
    }

    // Cancel request
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelRequest(@PathVariable Long id) {
        friendRequestService.cancelRequest(id);
        return ResponseEntity.ok().build();
    }

    // Get pending requests for a user
    @GetMapping("/{username}/pending")
    public ResponseEntity<List<FriendRequest>> getPendingRequests(@PathVariable String username) {
        return ResponseEntity.ok(friendRequestService.getPendingRequests(username));
    }
}
