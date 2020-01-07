package com.jblupus.twittercrawler.dao;

import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.WriteTimeoutException;
import com.jblupus.twittercrawler.model.ScheduledUser;

import java.util.List;

/**
 * Created by joao on 11/30/16.
 */
public interface ScheduledUserDAO {
    void save(ScheduledUser scheduledUser) throws WriteTimeoutException, NoHostAvailableException;

    List<ScheduledUser> findById(Long datasetId, String information, Long referenceId, int limit);

    ScheduledUser findOnde(Long datasetId, String information,long id);
}
