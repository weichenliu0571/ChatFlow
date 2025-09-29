package com.chatflow.chatflow.model;

import jakarta.persistence.*;

@Entity
@Table(name = "friendships")
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String requester; // who added the friend

    @Column(nullable = false)
    private String addressee; // the friend being added

    public Friendship() {
    }

    public Friendship(String requester, String addressee) {
        this.requester = requester;
        this.addressee = addressee;
    }

    public Long getId() {
        return id;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }
}
