package com.chatflow.chatflow.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatflow.chatflow.model.User;
import com.chatflow.chatflow.repository.UserProfileRepository;

@Service
public class UserProfileService {

    private final UserProfileRepository repo;

    public UserProfileService(UserProfileRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public String getAvatarUrl(String username) {
        return repo.findAvatarByUsername(username).orElse(null);
    }

    @Transactional
    public void setAvatarUrl(String username, String url) {
        User u = repo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        u.setAvatar(url);
        
        //optional
        repo.save(u);
    }

    @Transactional(readOnly = true)
    public List<String> getAllowedAvatarChoices() {
        return List.of(
            "/avatar/cat.png",
            "/avatar/dog.png",
            "/avatar/turtle.png"
        );
    }

    public boolean isValidAvatarPath(String avatarUrl) {
        return avatarUrl != null && avatarUrl.startsWith("/avatar/");
    }

}
