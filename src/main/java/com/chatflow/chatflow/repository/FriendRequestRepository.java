package com.chatflow.chatflow.repository;

import com.chatflow.chatflow.model.FriendRequest;
import com.chatflow.chatflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    // Find all requests received by a user
    List<FriendRequest> findByAddressee(User addressee);

    // Find all requests sent by a user
    List<FriendRequest> findByRequester(User requester);

    // Find request between two specific users
    Optional<FriendRequest> findByRequesterAndAddressee(User requester, User addressee);

    // Find all pending requests for a user
    List<FriendRequest> findByAddresseeAndStatus(User addressee, FriendRequest.Status status);
}
