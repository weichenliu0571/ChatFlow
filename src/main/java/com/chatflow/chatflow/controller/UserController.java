package com.chatflow.chatflow.controller;

// principal is current authenticated user
import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatflow.chatflow.model.User;
import com.chatflow.chatflow.repository.UserRepository;

@RestController @RequestMapping("/users")
public class UserController {
    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // autocomplete endpoint
    @GetMapping("/suggest")
    public List<String> suggestUsers(@RequestParam(required = false) String q, Principal principal) {
        String query = (q == null) ? "": q.trim();

        // to avoid expensive queries
        if (query.length() < 2) {
            return List.of();
        }

        // transforming the data to a stream allows us to transform the data (in this case we just have a list of usernames from the user models)
        List<String> names = userRepo.findTop10ByUsernameStartingWithIgnoreCaseOrderByUsernameAsc(query)
            .stream()
            .map(User::getUsername)
            .toList();

        // to remove the current user
        if (principal != null) {
            String me = principal.getName();
            names = names.stream()
                .filter(n -> !n.equalsIgnoreCase(me)) // only keeps if the name is not equal to me
                .toList();
        }

        return names;
    }

}
