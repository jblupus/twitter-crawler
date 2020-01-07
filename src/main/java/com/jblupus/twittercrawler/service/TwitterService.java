package com.jblupus.twittercrawler.service;

import twitter4j.*;

import java.util.List;

/**
 * Created by joao on 11/30/16.
 */
public interface TwitterService {
    User showUser(Long id, Twitter twitter) throws TwitterException;
    List<Long> collectFriendships(Long userId, Twitter twitter) throws TwitterException;
    List<Status> collectLikes(Long userId, Paging paging, Twitter twitter) throws TwitterException;

    List<Status> collectTweets(Long userId, Paging paging, Twitter twitter) throws TwitterException;

}
