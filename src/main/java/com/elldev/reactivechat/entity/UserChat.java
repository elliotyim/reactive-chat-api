package com.elldev.reactivechat.entity;

import com.elldev.reactivechat.entity.key.UserChatKey;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "user_chats")
@Getter
public class UserChat {
    @EmbeddedId
    private UserChatKey id;

    private LocalDateTime createdAt;

    @ManyToOne
    @MapsId("chatId")
    @JoinColumn(name = "chat_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void createdAt() {
        this.createdAt = LocalDateTime.now();
    }
}
