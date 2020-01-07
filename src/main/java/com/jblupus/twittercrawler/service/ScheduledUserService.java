package com.jblupus.twittercrawler.service;

import com.jblupus.twittercrawler.model.ScheduledUser;

import java.util.List;

/**
 * Created by joao on 11/30/16.
 */
public interface ScheduledUserService {
    void save(ScheduledUser scheduledUser);
    List<ScheduledUser> findById(Long datasetId, String information, Long referenceId, int limit);

    ScheduledUser findOne(Long datasetId, String information, long id);
}
