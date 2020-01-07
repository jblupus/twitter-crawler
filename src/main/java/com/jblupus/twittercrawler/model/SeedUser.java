package com.jblupus.twittercrawler.model;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by joao on 11/29/16.
 */
@Table(value = "seed_user")
public class SeedUser {
    @PrimaryKey
    private SeedUserKey key;
    @Column(value = "is_active")
    private boolean isActive;
    @Column(value = "is_scheduled")
    private boolean isScheduled;

    public SeedUser() {
    }

    public SeedUser(SeedUserKey key, boolean isActive, boolean isScheduled) {
        this.key = key;
        this.isActive = isActive;
        this.isScheduled = isScheduled;
    }


    public SeedUserKey getKey() {
        return key;
    }

    public void setKey(SeedUserKey key) {
        this.key = key;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isScheduled() {
        return isScheduled;
    }

    public void setScheduled(boolean scheduled) {
        isScheduled = scheduled;
    }
}
