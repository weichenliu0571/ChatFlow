package com.chatflow.chatflow.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SettingsController {

    private final UserDetailsManager users;

    public SettingsController(UserDetailsManager users) {
        this.users = users;
    }

    // Show settings page
    @GetMapping("/settings")
    public String settingsPage() {
        return "settings"; // renders settings.html
    }

    // Stub for Change Password
    @PostMapping("/settings/change-password")
    public String changePassword(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Password change feature not implemented yet.");
        return "redirect:/settings";
    }

    // Delete Account works
    @PostMapping("/settings/delete-account")
    public String deleteAccount(@AuthenticationPrincipal UserDetails user,
                                RedirectAttributes redirectAttributes) {
        if (user != null) {
            users.deleteUser(user.getUsername());
        }
        return "redirect:/login?accountDeleted=true";
    }
}
