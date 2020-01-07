package com.jblupus.twittercrawler.service;

import com.jblupus.twittercrawler.model.Tweet;
import twitter4j.Status;

import java.util.List;

/**
 * Created by joao on 12/1/16.
 */
public interface TweetsService {
    void saveTweets(Long userId, List<Status> tweets);
    void save(Tweet tweet);
    List<Status> findUserRetweets(Long userId);
    List<Status> findUserTweets(Long userId);
    List<Status> findUserMentions(Long userId);
    List<Status> findUserMentions(List<Status> statuses);
    List<Status> findUserRetweets(List<Status> statuses);
}
