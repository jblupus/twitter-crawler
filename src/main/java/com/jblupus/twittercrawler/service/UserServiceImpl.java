package com.jblupus.twittercrawler.service;

import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.WriteTimeoutException;
import com.jblupus.twittercrawler.dao.UserDAO;
import com.jblupus.twittercrawler.model.TwitterUser;
import com.jblupus.twittercrawler.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by joao on 11/30/16.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO;

    @Override
    public void save(TwitterUser user) {
        while (true) {
            try {
                userDAO.save(user);
                break;
            } catch (WriteTimeoutException|NoHostAvailableException ex) {
                TimeUtils.sleep(TimeUtils.SLEEP_TIME_MIN);
            }
        }
    }
}
