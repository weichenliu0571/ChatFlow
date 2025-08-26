package com.chatflow.chatflow.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chatflow.chatflow.auth.ChangePasswordForm;

import jakarta.validation.Valid;

@Controller
public class SettingsController {

    private final UserDetailsManager users;
    private final PasswordEncoder encoder;

    public SettingsController(UserDetailsManager users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    // Main settings Page
    @GetMapping("/settings")
    public String settingsPage() {
        return "settings"; // renders settings.html
    }

    // Show change password form
    @GetMapping("/change-password")
    public String passwordForm(Model model) {
        model.addAttribute("form", new ChangePasswordForm());
        return "change-password"; // renders change-password.html
    }

    // Handle change password form POST
    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal UserDetails principal,
                                 @Valid @ModelAttribute("form") ChangePasswordForm form,
                                 BindingResult binding,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (binding.hasErrors()) {
            return "change-password"; // redisplay form with validation errors
        }

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            binding.rejectValue("confirmPassword", "mismatch", "Passwords do not match.");
            return "change-password";
        }

        // Verify current password
        UserDetails current = users.loadUserByUsername(principal.getUsername());
        if (!encoder.matches(form.getCurrentPassword(), current.getPassword())) {
            binding.rejectValue("currentPassword", "invalid", "Current password is incorrect.");
            return "change-password";
        }

        // Encode and update
        String encoded = encoder.encode(form.getNewPassword());
        UserDetails updated = User.withUserDetails(current).password(encoded).build();
        users.updateUser(updated);

        redirectAttributes.addFlashAttribute("success", "Password updated successfully.");
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
