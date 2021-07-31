package com.elldev.reactivechat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class User {
    @Id
    private String id;

    private String name;
    private String email;
    private String password;
    private String profileImg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    private Set<Message> messages;

    @OneToMany(mappedBy = "user")
    private Set<UserChat> userChats;

    @OneToMany(mappedBy = "friendOne")
    private Set<Friendship> friendshipsOne;

    @OneToMany(mappedBy = "friendTwo")
    private Set<Friendship> friendshipsTwo;

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public User setProfileImg(String profileImg) {
        this.profileImg = profileImg;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    @PrePersist
    public void createdAt() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void updatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}
