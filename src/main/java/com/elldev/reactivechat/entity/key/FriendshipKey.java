package com.elldev.reactivechat.entity.key;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FriendshipKey implements Serializable {
    private String friendOneId;
    private String friendTwoId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipKey that = (FriendshipKey) o;
        return Objects.equals(friendOneId, that.friendOneId) && Objects.equals(friendTwoId, that.friendTwoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendOneId, friendTwoId);
    }
}
