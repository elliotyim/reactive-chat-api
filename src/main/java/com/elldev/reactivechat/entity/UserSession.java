package com.elldev.reactivechat.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@RedisHash(value = "userSessions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserSession {
    @Id
    private Long id;

    @Id
    @Indexed
    private String token;

    @Indexed
    private String userId;

    private String name;
    private String email;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @TimeToLive(unit = TimeUnit.DAYS)
    @Builder.Default
    private Long timeToLive = 2l;
}
