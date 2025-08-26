package com.chatflow.chatflow.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.chatflow.chatflow.profile.UserProfileRepository;

@Controller
public class HomeController {

  private final UserProfileRepository repo;

  public HomeController(UserProfileRepository repo) {
    this.repo = repo;
  }

  @GetMapping("/")
  public String home(@AuthenticationPrincipal UserDetails user, Model model) {
    String username = (user != null) ? user.getUsername() : null;
    model.addAttribute("username", username);

    String avatarUrl = (username != null) ? repo.getAvatarUrl(username) : null;
    model.addAttribute("avatarUrl", avatarUrl);   // let the view handle fallback
    return "index"; // your home template
  }
}