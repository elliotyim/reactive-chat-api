package com.elldev.reactivechat.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "chat_room")
@Getter
public class ChatRoom {
    @Id
    private String guid;

    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "chatRoom")
    private Set<Message> messages;

    @OneToMany(mappedBy = "chatRoom")
    private Set<UserChat> userChats;
}
