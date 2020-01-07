package com.jblupus.twittercrawler.model;

import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import java.io.Serializable;

/**
 * Created by joao on 11/30/16.
 */
@PrimaryKeyClass
public class TwitterUserKey implements Serializable{
    @PrimaryKeyColumn(name = "dataset_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private Long databaseId;
    @PrimaryKeyColumn(name = "user_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    private Long userId;

    public TwitterUserKey() {
    }

    public TwitterUserKey(Long databaseId, Long userId) {
        this.databaseId = databaseId;
        this.userId = userId;
    }

    public Long getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Long databaseId) {
        this.databaseId = databaseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TwitterUserKey that = (TwitterUserKey) o;

        if (!databaseId.equals(that.databaseId)) return false;
        return userId.equals(that.userId);

    }

    @Override
    public int hashCode() {
        int result = databaseId.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
    }
}
