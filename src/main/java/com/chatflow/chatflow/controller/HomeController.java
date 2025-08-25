package com.chatflow.chatflow.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping("/")
  public String home(@AuthenticationPrincipal UserDetails user, Model model) {
    if (user != null) {
      model.addAttribute("username", user.getUsername());
    } else {
      // Always add username to avoid Thymeleaf errors
      model.addAttribute("username", "Guest");
    }
    return "index";
  }
}
