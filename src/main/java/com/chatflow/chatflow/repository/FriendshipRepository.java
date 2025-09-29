package com.chatflow.chatflow.repository;

import com.chatflow.chatflow.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByRequester(String requester);
}
