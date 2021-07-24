package com.elldev.reactivechat.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "chat_rooms")
@Getter
public class ChatRoom {
    @Id
    private String id;

    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "chatRoom")
    private Set<Message> messages;

    @OneToMany(mappedBy = "chatRoom")
    private Set<UserChat> userChats;

    @PrePersist
    public void createdAt() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void updatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}
