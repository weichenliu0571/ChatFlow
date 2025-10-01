package com.chatflow.chatflow.service;

import com.chatflow.chatflow.model.FriendRequest;
import com.chatflow.chatflow.model.User;
import com.chatflow.chatflow.repository.FriendRequestRepository;
import com.chatflow.chatflow.repository.FriendshipRepository;
import com.chatflow.chatflow.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    public FriendRequestService(FriendRequestRepository friendRequestRepository,
            FriendshipRepository friendshipRepository,
            UserRepository userRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    // Send a new request
    public FriendRequest sendRequest(String requester, String addressee) {
        if (requester.equals(addressee)) {
            throw new IllegalArgumentException("Cannot send a request to yourself");
        }

        User requesterUser = userRepository.findByUsername(requester)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + requester));

        User addresseeUser = userRepository.findByUsername(addressee)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + addressee));

        // check if request already exists
        if (friendRequestRepository.findByRequesterAndAddressee(requesterUser, addresseeUser).isPresent()) {
            throw new IllegalStateException("Request already exists");
        }

        FriendRequest request = new FriendRequest();
        request.setRequester(requesterUser);
        request.setAddressee(addresseeUser);
        request.setStatus(FriendRequest.Status.PENDING);
        return friendRequestRepository.save(request);
    }

    // Accept a request
    public void acceptRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        request.setStatus(FriendRequest.Status.ACCEPTED);
        request.setRespondedAt(java.time.OffsetDateTime.now());
        friendRequestRepository.save(request);

        // Create friendship record
        friendshipRepository.save(new com.chatflow.chatflow.model.Friendship() {
            {
                setUser(request.getRequester());
                setFriend(request.getAddressee());
            }
        });
        friendshipRepository.save(new com.chatflow.chatflow.model.Friendship() {
            {
                setUser(request.getAddressee());
                setFriend(request.getRequester());
            }
        });
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
