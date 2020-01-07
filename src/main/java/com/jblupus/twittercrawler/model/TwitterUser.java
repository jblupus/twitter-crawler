package com.jblupus.twittercrawler.model;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by joao on 11/30/16.
 */
@Table("twitter_user")
public class TwitterUser {

    @PrimaryKey
    private TwitterUserKey key;

    @Column("user_jason")
    private String userJson;

    public TwitterUser(TwitterUserKey key, String userJson) {
        this.key = key;
        this.userJson = userJson;
    }

    public TwitterUserKey getKey() {
        return key;
    }

    public void setKey(TwitterUserKey key) {
        this.key = key;
    }

    public String getUserJson() {
        return userJson;
    }

    public void setUserJson(String userJson) {
        this.userJson = userJson;
    }
}
