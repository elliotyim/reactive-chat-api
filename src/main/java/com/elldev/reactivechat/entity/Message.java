package com.elldev.reactivechat.entity;

import com.elldev.reactivechat.entity.key.MessageKey;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "messages")
@Getter
public class Message {
    @EmbeddedId
    private MessageKey id;

    private String content;
    private Boolean isRead;
    private LocalDateTime createdAt;

    @ManyToOne
    @MapsId("chatGuid")
    @JoinColumn(name = "chat_guid")
    private ChatRoom chatRoom;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;
}
