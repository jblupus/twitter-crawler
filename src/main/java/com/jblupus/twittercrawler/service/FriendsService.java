package com.jblupus.twittercrawler.service;

import com.jblupus.twittercrawler.model.Friend;

import java.util.List;

/**
 * Created by joao on 12/1/16.
 */
public interface FriendsService {
    void saveFriends(Long userId, List<Long> friendsList);
    void save(Friend friend);
    List<Friend> findUserFriends(Long userId);
}
