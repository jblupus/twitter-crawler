package com.jblupus.twittercrawler.jobs;

import com.google.gson.Gson;
import com.jblupus.twittercrawler.model.Keys;
import com.jblupus.twittercrawler.model.ScheduledUser;
import com.jblupus.twittercrawler.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by joao on 12/1/16.
 */
@Component
public class FriendshipCollector extends BaseJob implements Runnable {
    private static final Logger logger = Logger.getLogger(FriendshipCollector.class.getName());

    static final int MAX_COUNTER = 15;

    @Autowired
    private FriendsService friendsService;

    @Autowired
    private Gson gson;

    private User user;

    private List<Long> friendsIds;

    public void run(){
        Twitter twitter;
        Keys key = null;

        ScheduledUser scheduledUser = scheduledUserService.findOne(SeedsSaver.DATASET_ID, CrawlerScheduler.FRIENDS,user.getId());
        if(scheduledUser !=null){
            key = checkKey(key, MAX_COUNTER);
            twitter = connectionService.configureTwitter(key);
            Long userId = scheduledUser.getPk().getUserId();
            logger.log(Level.INFO, "Usuário id {0}, data {1}", new Object[]{userId, new Date()});

            if(!scheduledUser.isCollected()){

                do {
                    if (key.getCount() >= MAX_COUNTER) {
                        key = checkKey(key, MAX_COUNTER);
                        twitter = connectionService.configureTwitter(key);
                    }
                    try {
                        friendsIds = twitterService.collectFriendships(userId, twitter);
                        break;
                    } catch (TwitterException e) {
                        key.setCount(MAX_COUNTER);
                        key.setLastUsedAt(new Date());
                    }
                }while (true);

                key.setCount(key.getCount() + 1);
                key.setLastUsedAt(new Date());

                if(friendsIds!=null && !friendsIds.isEmpty()) saveFriends(userId, friendsIds);

                scheduledUser.setCollected(true);
                scheduledUser.setCollectionDate(new Date());
                scheduledUserService.save(scheduledUser);
            }
        }
    }

    private void saveFriends(Long userId, List<Long> friendsIds) {
        friendsService.saveFriends(userId, friendsIds);
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    void loadKeys(int maxCount) {
        super.loadKeys(maxCount);
    }

    public List<Long> getFriendsIds() {
        return friendsIds;
    }
}
