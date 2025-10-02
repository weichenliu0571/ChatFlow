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

import com.chatflow.chatflow.service.UserProfileService;

@Controller
public class ProfileController {

    private final UserProfileService service;

    public ProfileController(UserProfileService service) {
        this.service = service;
    }

    // Current Profile Picture
    @GetMapping("/profilepicture")
    public String profilePicture(@AuthenticationPrincipal UserDetails user, Model model) {
        String username = (user != null) ? user.getUsername() : "Guest";
        model.addAttribute("username", username);

        String avatarUrl = (user != null) ? service.getAvatarUrl(username) : null;
        model.addAttribute("avatarUrl", avatarUrl);
        return "profilepicture";
    }


    // Avatar Choices
    @GetMapping("/choose-avatar")
    public String chooseAvatarPage(@AuthenticationPrincipal UserDetails user, Model model) {
        if (user == null) return "redirect:/login";

        List<String> choices = service.getAllowedAvatarChoices();
        model.addAttribute("choices", choices);
        model.addAttribute("current", service.getAvatarUrl(user.getUsername()));
        return "choose-avatar";
    }

    // Save the selected avatar URL
    @PostMapping("/choose-avatar")
    public String chooseAvatar(@AuthenticationPrincipal UserDetails user,
                               @RequestParam("avatarUrl") String avatarUrl,
                               RedirectAttributes redirect) {
        if (user == null) return "redirect:/login";

        if (!service.isValidAvatarPath(avatarUrl)) {
            redirect.addFlashAttribute("error", "Invalid avatar selection.");
            return "redirect:/choose-avatar";
        }

        service.setAvatarUrl(user.getUsername(), avatarUrl);
        redirect.addFlashAttribute("success", "Profile picture updated!");
        return "redirect:/settings"; // or "redirect:/profilepicture"
    }
    
}
