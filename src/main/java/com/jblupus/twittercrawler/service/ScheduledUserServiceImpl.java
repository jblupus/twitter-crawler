package com.jblupus.twittercrawler.service;

import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.ReadTimeoutException;
import com.datastax.driver.core.exceptions.WriteTimeoutException;
import com.jblupus.twittercrawler.dao.ScheduledUserDAO;
import com.jblupus.twittercrawler.model.ScheduledUser;
import com.jblupus.twittercrawler.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by joao on 11/30/16.
 */
@Service
public class ScheduledUserServiceImpl implements ScheduledUserService {
    @Autowired
    private ScheduledUserDAO scheduledUserDAO;

    @Override
    public void save(ScheduledUser scheduledUser) {
        while (true) {
            try {
                scheduledUserDAO.save(scheduledUser);
                break;
            } catch (WriteTimeoutException|NoHostAvailableException ex) {
                TimeUtils.sleep(TimeUtils.SLEEP_TIME_MIN);
            }
        }
    }

    @Override
    public List<ScheduledUser> findById(Long datasetId, String information, Long referenceId, int limit) {

        while (true) {
            try {
                return scheduledUserDAO.findById(datasetId, information, referenceId, limit);
            } catch (ReadTimeoutException |NoHostAvailableException ex) {
                TimeUtils.sleep(TimeUtils.SLEEP_TIME_MIN);
            }
        }
    }

    @Override
    public ScheduledUser findOne(Long datasetId, String information, long id) {
        while (true) {
            try {
                return scheduledUserDAO.findOnde(datasetId, information, id);
            } catch (ReadTimeoutException |NoHostAvailableException ex) {
                TimeUtils.sleep(TimeUtils.SLEEP_TIME_MIN);
            }
        }

    }
}
