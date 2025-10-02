package com.chatflow.chatflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name = "users")
public class User {

    @Id @Column(length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "avatar")
    private String avatar;

    @Column(nullable = false)
    private boolean enabled;

    // requirement: no args constructor
    public User() {}

    public User(String username, String password, String avatar, boolean enabled) {
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.enabled = enabled;
    }

    // getters & setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
