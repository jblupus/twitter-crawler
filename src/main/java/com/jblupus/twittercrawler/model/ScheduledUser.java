package com.jblupus.twittercrawler.model;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.Date;

/**
 * Created by joao on 11/30/16.
 */
@Table("scheduled_user")
public class ScheduledUser {
    @PrimaryKey
    private ScheduledUserKey pk;
    @Column("is_collected")
    private boolean isCollected;
    @Column("collection_date")
    private Date collectionDate;


    public ScheduledUser() {
    }

    public ScheduledUser(ScheduledUserKey pk, boolean isCollected, Date collectionDate) {
        this.pk = pk;
        this.isCollected = isCollected;
        this.collectionDate = collectionDate;
    }

    public ScheduledUserKey getPk() {
        return pk;
    }

    public void setPk(ScheduledUserKey pk) {
        this.pk = pk;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public Date getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }
}
