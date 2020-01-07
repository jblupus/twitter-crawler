package com.jblupus.twittercrawler.dao;

import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.WriteTimeoutException;
import com.jblupus.twittercrawler.model.TwitterUser;

/**
 * Created by joao on 11/30/16.
 */
public interface UserDAO {
    void save(TwitterUser user) throws WriteTimeoutException, NoHostAvailableException;
}
