package com.chatflow.chatflow.service;

import com.chatflow.chatflow.model.FriendRequest;
import com.chatflow.chatflow.model.User;
import com.chatflow.chatflow.repository.FriendRequestRepository;
import com.chatflow.chatflow.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipService friendshipService;
    private final UserRepository userRepository;

    public FriendRequestService(FriendRequestRepository friendRequestRepository, FriendshipService friendshipService, UserRepository userRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.friendshipService = friendshipService;
        this.userRepository = userRepository;
    }

    @Transactional
    public FriendRequest sendRequest(String requester, String addressee) {
        // check if user sending request to themselves
        if (requester.equals(addressee)) {
            throw new IllegalArgumentException("Cannot send a request to yourself");
        }

        // check if requester or addressee are not valid
        User requesterUser = userRepository.findByUsername(requester)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + requester));

        User addresseeUser = userRepository.findByUsername(addressee)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + addressee));

        if (friendshipService.areFriends(requesterUser, addresseeUser)) {
            throw new IllegalArgumentException("Already Friends!");
        }

        // check if request already exists
        Optional<FriendRequest> existingOpt = friendRequestRepository.findByRequesterAndAddressee(requesterUser, addresseeUser);

        // if request exists and was not declined, we cannot send another request, otherwise if it exists, but was declined we send another
        if (existingOpt.isPresent()) {
            FriendRequest fr = existingOpt.get();
            if (fr.getStatus() != FriendRequest.Status.DECLINED && fr.getStatus() != FriendRequest.Status.CANCELED) {
                throw new IllegalStateException("Request already exists");
            }
            friendRequestRepository.delete(fr);
            friendRequestRepository.flush(); // makes sure delete happens first
        }

        FriendRequest request = new FriendRequest();
        request.setRequester(requesterUser);
        request.setAddressee(addresseeUser);
        request.setStatus(FriendRequest.Status.PENDING);
        return friendRequestRepository.save(request);
    }

    // Accept a request
    @Transactional
    public void acceptRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        request.setStatus(FriendRequest.Status.ACCEPTED);
        request.setRespondedAt(java.time.OffsetDateTime.now());
        friendRequestRepository.save(request);

        // Use abstracted service here
        friendshipService.createMutualFriendship(request.getRequester(), request.getAddressee());
    }

    // Decline a request
    public void declineRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        request.setStatus(FriendRequest.Status.DECLINED);
        request.setRespondedAt(java.time.OffsetDateTime.now());
        friendRequestRepository.save(request);
    }

    // Cancel a request (by sender)
    public void cancelRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        request.setStatus(FriendRequest.Status.CANCELED);
        request.setRespondedAt(java.time.OffsetDateTime.now());
        friendRequestRepository.save(request);
    }

    // Get pending requests for a user
    public List<FriendRequest> getPendingRequests(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        return friendRequestRepository.findByAddresseeAndStatus(user, FriendRequest.Status.PENDING);
    }

}
