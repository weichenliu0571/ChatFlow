package com.chatflow.chatflow.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "friendships", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_username", "friend_username" })
})
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One user in the friendship
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_username", nullable = false)
    private User user;

    // The other user in the friendship
    @ManyToOne(optional = false)
    @JoinColumn(name = "friend_username", nullable = false)
    private User friend;

    @Column(name = "since", nullable = false, columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime since = OffsetDateTime.now();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public OffsetDateTime getSince() {
        return since;
    }

    public void setSince(OffsetDateTime since) {
        this.since = since;
    }
}
