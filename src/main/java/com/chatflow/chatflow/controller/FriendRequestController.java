package com.chatflow.chatflow.controller;

import com.chatflow.chatflow.model.FriendRequest;
import com.chatflow.chatflow.service.FriendRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friend-requests")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    // Send a new request
    @PostMapping("/{requester}/{addressee}")
    public ResponseEntity<FriendRequest> sendRequest(
            @PathVariable String requester,
            @PathVariable String addressee) {
        return ResponseEntity.ok(friendRequestService.sendRequest(requester, addressee));
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
