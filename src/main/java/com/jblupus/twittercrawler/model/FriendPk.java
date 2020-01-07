package com.jblupus.twittercrawler.model;

import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import java.io.Serializable;

/**
 * Created by joao on 12/1/16.
 */
@PrimaryKeyClass
public class FriendPk implements Serializable {
    @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private Long userId;

    @PrimaryKeyColumn(name = "friend_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    private Long friendId;


    public FriendPk() {
    }

    public FriendPk(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendPk friendPk = (FriendPk) o;

        if (!userId.equals(friendPk.userId)) return false;
        return friendId.equals(friendPk.friendId);

    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + friendId.hashCode();
        return result;
    }
}
