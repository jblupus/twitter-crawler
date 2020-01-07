package com.jblupus.twittercrawler.service;

import com.jblupus.twittercrawler.model.TwitterUser;

/**
 * Created by joao on 11/30/16.
 */
public interface UserService {
    void save(TwitterUser user);
}
