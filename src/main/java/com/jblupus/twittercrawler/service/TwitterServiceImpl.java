package com.jblupus.twittercrawler.service;

import org.springframework.stereotype.Service;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao on 11/30/16.
 */
@Service
public class TwitterServiceImpl implements TwitterService {

    public User showUser(Long id, Twitter twitter) throws TwitterException {
        try {
            return twitter.showUser(id);
        } catch (TwitterException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 403){
                throw e;
            }
        }
        return null;
    }

    @Override
    public List<Long> collectFriendships(Long userId, Twitter twitter) throws TwitterException {
        List<Long> idsList = new ArrayList<>();
        try {
            IDs ids = twitter.friendsFollowers().getFriendsIDs(userId, -1, 5000);
            if(ids!=null) {
                for (long id:ids.getIDs()) idsList.add(id);
            }
        } catch (TwitterException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 403){
                throw e;
            }
        }
        return idsList;
    }

    @Override
    public List<Status> collectLikes(Long userId, Paging paging, Twitter twitter) throws TwitterException {
        try {
            return twitter.favorites().getFavorites(userId, paging);
        } catch (TwitterException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 403){
                throw e;
            }
        }
        return null;
    }

    @Override
    public List<Status> collectTweets(Long userId, Paging paging, Twitter twitter) throws TwitterException {
        try {
            return twitter.getUserTimeline(userId, paging);
        } catch (TwitterException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 403){
                throw e;
            }
        }
        return null;
    }
}
