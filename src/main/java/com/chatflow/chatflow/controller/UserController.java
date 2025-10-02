package com.chatflow.chatflow.controller;

// principal is current authenticated user
import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatflow.chatflow.service.UserSuggestionService;

@RestController @RequestMapping("/users")
public class UserController {
    
    private final UserSuggestionService suggestionService;

    public UserController(UserSuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    // autocomplete endpoint
    @GetMapping("/suggest")
    public List<String> suggestUsers(@RequestParam(required = false) String q, Principal principal) {
        
        String me = (principal != null) ? principal.getName() : null;
        return suggestionService.suggestUsernames(q,me);

    }

}
