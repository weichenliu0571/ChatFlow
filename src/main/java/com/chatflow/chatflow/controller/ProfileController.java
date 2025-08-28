package com.chatflow.chatflow.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chatflow.chatflow.profile.UserProfileRepository;

@Controller
public class ProfileController {

    private final UserProfileRepository repo;

    public ProfileController(UserProfileRepository repo) {
        this.repo = repo;
    }

    // Current Profile Picture
    @GetMapping("/profilepicture")
    public String profilePicture(@AuthenticationPrincipal UserDetails user, Model model) {
        String username = (user != null) ? user.getUsername() : "Guest";
        model.addAttribute("username", username);

        String avatarUrl = (user != null) ? repo.getAvatarUrl(username) : null;
        model.addAttribute("avatarUrl", avatarUrl);
        return "profilepicture";
    }

    // Avatar Choices
    @GetMapping("/choose-avatar")
    public String chooseAvatarPage(@AuthenticationPrincipal UserDetails user, Model model) {
        if (user == null)
            return "redirect:/login";

        // Update this list as you add files under src/main/resources/static/avatar/
        List<String> choices = List.of(
                "/avatar/cat.png",
                "/avatar/dog.png",
                "/avatar/turtle.png");
        model.addAttribute("choices", choices);
        model.addAttribute("current", repo.getAvatarUrl(user.getUsername()));
        return "choose-avatar";
    }

    // Save the selected avatar URL (stored in users.avatar_url)
    @PostMapping("/choose-avatar")
    public String chooseAvatar(@AuthenticationPrincipal UserDetails user,
            @RequestParam("avatarUrl") String avatarUrl,
            RedirectAttributes redirect) {
        if (user == null)
            return "redirect:/login";

        // Path Validation
        if (avatarUrl == null || !avatarUrl.startsWith("/avatar/")) {
            redirect.addFlashAttribute("error", "Invalid avatar selection.");
            return "redirect:/choose-avatar";
        }

        repo.setAvatarUrl(user.getUsername(), avatarUrl);
        redirect.addFlashAttribute("success", "Profile picture updated!");
        return "redirect:/settings"; // or "redirect:/profilepicture" if you prefer
    }
}
