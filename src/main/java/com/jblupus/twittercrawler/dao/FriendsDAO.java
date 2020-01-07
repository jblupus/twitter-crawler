package com.jblupus.twittercrawler.dao;

import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.WriteTimeoutException;
import com.jblupus.twittercrawler.model.Friend;

import java.util.List;

/**
 * Created by joao on 12/1/16.
 */
public interface FriendsDAO {
    void save(Friend friend) throws WriteTimeoutException, NoHostAvailableException;

    List<Friend> findUserFriends(Long userId, Long baseFriendId);
}
