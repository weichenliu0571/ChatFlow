package com.chatflow.chatflow.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    // In-memory chat history
    private final Map<String, List<String>> chats = new HashMap<>();

    // Send message
    @PostMapping("/send")
    public String sendMessage(@RequestParam String recipient,
            @RequestParam String message) {
        chats.computeIfAbsent(recipient, k -> new ArrayList<>()).add("You: " + message);
        return "Message sent to " + recipient;
    }

    // Retrieve chat history
    @GetMapping("/history")
    public List<String> getHistory(@RequestParam String recipient) {
        return chats.getOrDefault(recipient, new ArrayList<>());
    }
}
