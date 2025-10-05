package com.chatflow.chatflow.controller;

import com.chatflow.chatflow.model.FriendRequest;
import com.chatflow.chatflow.model.Friendship;
import com.chatflow.chatflow.service.FriendRequestService;
import com.chatflow.chatflow.service.FriendshipService;
import com.chatflow.chatflow.service.UserProfileService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final UserProfileService userProfileService;
    private final FriendRequestService friendRequestService;
    private final FriendshipService friendshipService;

    public HomeController(UserProfileService userProfileService,
                          FriendRequestService friendRequestService,
                          FriendshipService friendshipService) {
        this.userProfileService = userProfileService;
        this.friendRequestService = friendRequestService;
        this.friendshipService = friendshipService;
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetails user, Model model) {
        String username = (user != null) ? user.getUsername() : null;
        model.addAttribute("username", username);

        String avatarUrl = (username != null) ? userProfileService.getAvatarUrl(username) : null;
        model.addAttribute("avatarUrl", avatarUrl);

        return "index";
    }

    @GetMapping("/friends")
    public String friends(@AuthenticationPrincipal UserDetails user, Model model) {
        String username = (user != null) ? user.getUsername() : null;
        model.addAttribute("username", username);

        String avatarUrl = (username != null) ? userProfileService.getAvatarUrl(username) : null;
        model.addAttribute("avatarUrl", avatarUrl);

        if (username != null) {
            List<FriendRequest> pendingRequests = friendRequestService.getPendingRequests(username);
            List<Friendship> friends = friendshipService.getFriends(username);

            model.addAttribute("pendingRequests", pendingRequests);
            model.addAttribute("friends", friends);
        }

        return "friends";
    }
}
