package com.chatflow.chatflow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.chatflow.chatflow.model.User;

public interface UserProfileRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    @Query("select u.avatar from User u where u.username = :username")
    Optional<String> findAvatarByUsername(String username);

}
