package com.jblupus.twittercrawler.dao;

import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.WriteTimeoutException;
import com.jblupus.twittercrawler.model.Tweet;

import java.util.List;

/**
 * Created by joao on 12/1/16.
 */
public interface TweetsDAO {
    void save(Tweet tweet) throws WriteTimeoutException, NoHostAvailableException;

    List<Tweet> findTweets(Long userId, long baseTweetId);
}
