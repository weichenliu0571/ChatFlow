package com.chatflow.chatflow.service;

import com.chatflow.chatflow.model.Friendship;
import com.chatflow.chatflow.model.User;
import com.chatflow.chatflow.repository.FriendshipRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FriendshipService {
        private final FriendshipRepository friendshipRepository;

        public FriendshipService(FriendshipRepository friendshipRepository) {
                this.friendshipRepository = friendshipRepository;
        }

        /**
         * Create a mutual friendship (A ↔ B).
         */
        @Transactional
        public void createMutualFriendship(User user1, User user2) {
                // Prevent duplicates
                if (!friendshipRepository.existsByUserAndFriend(user1, user2)) {
                Friendship f1 = new Friendship();
                f1.setUser(user1);
                f1.setFriend(user2);
                friendshipRepository.save(f1);
                }

                if (!friendshipRepository.existsByUserAndFriend(user2, user1)) {
                Friendship f2 = new Friendship();
                f2.setUser(user2);
                f2.setFriend(user1);
                friendshipRepository.save(f2);
                }
        }

        /**
         * Optional helper for removing a friendship (if you ever add an "unfriend" feature).
         */
        @Transactional
        public void removeMutualFriendship(User user1, User user2) {
                friendshipRepository.findByUserAndFriend(user1, user2)
                        .ifPresent(friendshipRepository::delete);
                friendshipRepository.findByUserAndFriend(user2, user1)
                        .ifPresent(friendshipRepository::delete);
        }

        // Get all friendships for a user (the "other side" is in getFriend())
        @Transactional(readOnly = true)
                public List<Friendship> findFriendsOf(User user) {
                return friendshipRepository.findByUser(user);
        }

        // FriendshipService
        public boolean areFriends(User a, User b) {
                return friendshipRepository.existsByUserAndFriend(a, b)
                || friendshipRepository.existsByUserAndFriend(b, a);
        }
}
