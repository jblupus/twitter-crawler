package com.jblupus.twittercrawler.model;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by joao on 12/1/16.
 */
@Table("friend")
public class Friend {
    @PrimaryKey
    private FriendPk key;

    public Friend() {
    }

    public Friend(FriendPk key) {
        this.key = key;
    }

    public FriendPk getKey() {
        return key;
    }

    public void setKey(FriendPk key) {
        this.key = key;
    }
}
