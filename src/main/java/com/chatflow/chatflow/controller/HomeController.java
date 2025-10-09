package com.chatflow.chatflow.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.chatflow.chatflow.model.Friendship;
import com.chatflow.chatflow.model.User;
import com.chatflow.chatflow.service.UserProfileService;
import com.chatflow.chatflow.service.FriendRequestService;
import com.chatflow.chatflow.service.FriendshipService;

@Controller
public class HomeController {

    private final UserProfileService userProfileService;
    private final FriendshipService friendshipService;
    private final FriendRequestService friendRequestService;

    public HomeController(UserProfileService userProfileService, FriendshipService friendshipService,
            FriendRequestService friendRequestService) {
        this.userProfileService = userProfileService;
        this.friendshipService = friendshipService;
        this.friendRequestService = friendRequestService;
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetails user, Model model) {
        String username = (user != null) ? user.getUsername() : null;
        model.addAttribute("username", username);

        String avatarUrl = (username != null) ? userProfileService.getAvatarUrl(username) : null;
        model.addAttribute("avatarUrl", avatarUrl);

        if (username != null) {
            User currentUser = userProfileService.getUser(username);
            List<Friendship> allFriends = friendshipService.findFriendsOf(currentUser);
            model.addAttribute("friendsPreview", allFriends.stream().limit(10).toList());
        } else {
            model.addAttribute("friendsPreview", List.of());
        }

        return "index";
    }

    @GetMapping("/friends")
    public String friends(@AuthenticationPrincipal UserDetails user, Model model) {
        String username = (user != null) ? user.getUsername() : null;
        model.addAttribute("username", username);
        model.addAttribute("avatarUrl",
                (username != null) ? userProfileService.getAvatarUrl(username) : null);

        if (username != null) {
            User currentUser = userProfileService.getUser(username);
            model.addAttribute("friends", friendshipService.findFriendsOf(currentUser));
            model.addAttribute("pendingRequests", friendRequestService.getPendingRequests(username));
        } else {
            model.addAttribute("friends", List.of());
            model.addAttribute("pendingRequests", List.of());
        }

        return "friends";
    }

    @GetMapping("/chat/{friendUsername}")
    public String chatWithFriend(@PathVariable String friendUsername,
            @AuthenticationPrincipal UserDetails user,
            Model model) {
        String username = (user != null) ? user.getUsername() : null;
        model.addAttribute("username", username);
        model.addAttribute("avatarUrl",
                (username != null) ? userProfileService.getAvatarUrl(username) : null);
        model.addAttribute("chatFriend", friendUsername);

        if (username != null) {
            User currentUser = userProfileService.getUser(username);
            List<Friendship> allFriends = friendshipService.findFriendsOf(currentUser);
            model.addAttribute("friendsPreview", allFriends.stream().limit(10).toList());
        } else {
            model.addAttribute("friendsPreview", List.of());
        }

        return "index";
    }
}
