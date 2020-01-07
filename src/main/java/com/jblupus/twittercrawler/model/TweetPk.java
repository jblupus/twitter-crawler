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
public class TweetPk implements Serializable {
    @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private Long userId;

    @PrimaryKeyColumn(name = "tweet_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    private Long tweet_id;

    public TweetPk() {
    }

    public TweetPk(Long userId, Long tweet_id) {
        this.userId = userId;
        this.tweet_id = tweet_id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTweet_id() {
        return tweet_id;
    }

    public void setTweet_id(Long tweet_id) {
        this.tweet_id = tweet_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TweetPk likePk = (TweetPk) o;

        if (!userId.equals(likePk.userId)) return false;
        return tweet_id.equals(likePk.tweet_id);

    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + tweet_id.hashCode();
        return result;
    }
}
