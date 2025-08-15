package com.chatflow.chatflow.controller;

import com.chatflow.chatflow.auth.SignupForm;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

  private final UserDetailsManager users;
  private final PasswordEncoder encoder;

  public SignupController(UserDetailsManager users, PasswordEncoder encoder) {
    this.users = users;
    this.encoder = encoder;
  }

  @GetMapping("/signup")
  public String signupForm(Model model) {
    model.addAttribute("signupForm", new SignupForm());
    return "signup";
  }

  @PostMapping("/signup")
  public String handleSignup(@Valid SignupForm form, BindingResult binding, Model model) {
    if (binding.hasErrors()) return "signup";

    if (!form.getPassword().equals(form.getConfirmPassword())) {
      binding.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match");
      return "signup";
    }

    if (users.userExists(form.getUsername())) {
      binding.rejectValue("username", "username.taken", "Username is already taken");
      return "signup";
    }

    UserDetails user = User.withUsername(form.getUsername())
        .password(encoder.encode(form.getPassword()))
        .roles("USER")
        .build();

    users.createUser(user);
    return "redirect:/login?registered=true";
  }
}
