package com.chatflow.chatflow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatflow.chatflow.model.User;

// Manages User entities with primary key type String
public interface UserRepository extends JpaRepository<User, String> {

    // method name magic, springboot knows from the findBy<> to know what to select.
    Optional<User> findByUsername(String username);

    // returns a list of users without case capitalization
    List<User> findByUsernameContainingIgnoreCase(String query);

    // prefix and limit method
    List<User> findTop10ByUsernameStartingWithIgnoreCaseOrderByUsernameAsc(String prefix);

}
