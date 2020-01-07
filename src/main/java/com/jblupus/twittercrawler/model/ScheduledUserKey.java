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
public class ScheduledUserKey implements Serializable {

    @PrimaryKeyColumn(name = "dataset_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private Long datasetId;

    @PrimaryKeyColumn(name = "user_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    private Long userId;

    @PrimaryKeyColumn(name = "information", ordinal = 1, type = PrimaryKeyType.PARTITIONED, ordering = Ordering.ASCENDING)
    private String information;


    public ScheduledUserKey() {
    }

    public ScheduledUserKey(Long datasetId, Long userId, String information) {
        this.datasetId = datasetId;
        this.userId = userId;
        this.information = information;
    }

    public Long getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(Long datasetId) {
        this.datasetId = datasetId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScheduledUserKey that = (ScheduledUserKey) o;

        if (!datasetId.equals(that.datasetId)) return false;
        if (!userId.equals(that.userId)) return false;
        return information.equals(that.information);

    }

    @Override
    public int hashCode() {
        int result = datasetId.hashCode();
        result = 31 * result + userId.hashCode();
        result = 31 * result + information.hashCode();
        return result;
    }
}
