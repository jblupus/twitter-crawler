package com.jblupus.twittercrawler.service;

import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.ReadTimeoutException;
import com.datastax.driver.core.exceptions.WriteTimeoutException;
import com.jblupus.twittercrawler.model.FriendPk;
import com.jblupus.twittercrawler.dao.FriendsDAO;
import com.jblupus.twittercrawler.model.Friend;
import com.jblupus.twittercrawler.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao on 12/1/16.
 */
@Service
public class FriendsServiceImpl implements FriendsService {
    @Autowired
    private FriendsDAO friendsDAO;

    @Override
    public void saveFriends(Long userId, List<Long> friendsList) {
        for(Long friend:friendsList){
            save(new Friend(new FriendPk(userId, friend)));
        }
    }

    @Override
    public void save(Friend friend) {
        while (true) {
            try {
                friendsDAO.save(friend);
                break;
            } catch (WriteTimeoutException|NoHostAvailableException ex) {
                TimeUtils.sleep(TimeUtils.SLEEP_TIME_MIN);
            }
        }

    }

    @Override
    public List<Friend> findUserFriends(Long userId) {

        List<Friend> friends = new ArrayList<>();
        long baseFriendId = 0L;
            while (true) {
                List<Friend> ret = findUserFriends(userId, baseFriendId);
                if(ret == null || ret.isEmpty()) break;

                friends.addAll(ret);

                baseFriendId = ret.get(ret.size()-1).getKey().getFriendId();
            }
        return friends;
    }

    private List<Friend> findUserFriends(Long userId, Long baseFriendId) {
        while (true) {
            try {
                return friendsDAO.findUserFriends( userId, baseFriendId);
            } catch (ReadTimeoutException | NoHostAvailableException ex) {
                ex.printStackTrace();
                TimeUtils.sleep(TimeUtils.SLEEP_TIME_MIN);
            }
        }
    }
}
