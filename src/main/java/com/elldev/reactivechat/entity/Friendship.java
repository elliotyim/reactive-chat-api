package com.elldev.reactivechat.entity;

import com.elldev.reactivechat.entity.key.FriendshipKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "friendships")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Friendship {
    @EmbeddedId
    private FriendshipKey id;

    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @MapsId("friendOneId")
    @JoinColumn(name = "friend_one_id")
    private User friendOne;

    @ManyToOne
    @MapsId("friendTwoId")
    @JoinColumn(name = "friend_two_id")
    private User friendTwo;

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
