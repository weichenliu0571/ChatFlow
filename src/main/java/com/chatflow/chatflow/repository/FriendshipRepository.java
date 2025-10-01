package com.chatflow.chatflow.repository;

import com.chatflow.chatflow.model.Friendship;
import com.chatflow.chatflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    // Find all friendships for a given user
    List<Friendship> findByUser(User user);

    // Find specific friendship between two users
    Optional<Friendship> findByUserAndFriend(User user, User friend);

    // Check if two users are already friends
    boolean existsByUserAndFriend(User user, User friend);
}
