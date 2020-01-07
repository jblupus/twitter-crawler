package com.jblupus.twittercrawler.jobs;

import com.jblupus.twittercrawler.model.Keys;
import com.jblupus.twittercrawler.model.ScheduledUser;
import com.jblupus.twittercrawler.service.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.*;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by joao on 12/1/16.
 */
@Component
public class LikesCollector extends BaseJob implements Runnable {
    private static final Logger logger = Logger.getLogger(LikesCollector.class.getName());
    static final int MAX_COUNTER = 75;
    private static final int MAX_PAGES = 15;

    @Autowired
    private LikesService likesService;

    private User user;

    @Override
    public void run() {

        Twitter twitter;
        Keys key = null;
        ScheduledUser scheduledUser = scheduledUserService.findOne(SeedsSaver.DATASET_ID, CrawlerScheduler.LIKES, user.getId());
        if (scheduledUser!=null){
            key = checkKey(key, MAX_COUNTER);
            twitter = connectionService.configureTwitter(key);
            Long userId = scheduledUser.getPk().getUserId();
            logger.log(Level.INFO, "Usuário id {0}, data {1}", new Object[]{userId, new Date()});
            if(!scheduledUser.isCollected()){
                for (int page = 1; page <= MAX_PAGES; page++ ) {
                    List<Status> likes;
                    do {
                        if (key.getCount() >= MAX_COUNTER) {
                            key = checkKey(key, MAX_COUNTER);
                            twitter = connectionService.configureTwitter(key);
                        }
                        try {
                            likes = twitterService.collectLikes(userId, new Paging(page, 200), twitter);
                            break;
                        } catch (TwitterException e) {
                            key.setCount(MAX_COUNTER);
                            key.setLastUsedAt(new Date());
                        }
                    }while (true);
                    key.setCount(key.getCount() + 1);
                    key.setLastUsedAt(new Date());

                    if(likes == null || likes.isEmpty()) break;
                    saveLikes(userId, likes);
                }
                scheduledUser.setCollected(true);
                scheduledUser.setCollectionDate(new Date());
                scheduledUserService.save(scheduledUser);
            }
        }
    }

    private void saveLikes(Long userId, List<Status> likes) {
        likesService.saveLikes(userId, likes);
    }

    @Override
    void loadKeys(int maxCount) {
        super.loadKeys(maxCount);
    }

    public void setUser(User user) {
        this.user = user;
    }
}
