package com.jblupus.twittercrawler.model;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by joao on 12/1/16.
 */
@Table("tweet")
public class Tweet {
    @PrimaryKey
    private TweetPk key;

    @Column("tweet_json")
    private String tweetJson;


    public Tweet() {
    }


    public Tweet(TweetPk key, String tweetJson) {
        this.key = key;
        this.tweetJson = tweetJson;
    }

    public TweetPk getKey() {
        return key;
    }

    public void setKey(TweetPk key) {
        this.key = key;
    }

    public String getTweetJson() {
        return tweetJson;
    }

    public void setTweetJson(String tweetJson) {
        this.tweetJson = tweetJson;
    }
}
