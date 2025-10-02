package com.chatflow.chatflow.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatflow.chatflow.model.User;
import com.chatflow.chatflow.repository.UserRepository;

@Service
public class UserSuggestionService {

    public static final int MIN_QUERY_LENGTH = 2;

    private final UserRepository userRepository;

    public UserSuggestionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<String> suggestUsernames(String rawQuery, String currentUsername) {
        String q = (rawQuery == null) ? "" : rawQuery.trim();
        if (q.length() < MIN_QUERY_LENGTH) {
            return List.of();
        }

        List<String> names = userRepository
            .findTop10ByUsernameStartingWithIgnoreCaseOrderByUsernameAsc(q)
            .stream()
            .map(User::getUsername)
            .toList();

        // if we currently are not logged in
        if (currentUsername == null || currentUsername.isBlank()) {
            return names;
        }

        return names.stream()
            .filter(n -> !n.equalsIgnoreCase(currentUsername))
            .toList();
    }


}
