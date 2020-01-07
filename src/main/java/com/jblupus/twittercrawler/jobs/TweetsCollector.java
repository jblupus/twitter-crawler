package com.jblupus.twittercrawler.jobs;

import com.jblupus.twittercrawler.service.TweetsService;
import com.jblupus.twittercrawler.model.Keys;
import com.jblupus.twittercrawler.model.ScheduledUser;
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
public class TweetsCollector extends BaseJob implements Runnable {
    private static final Logger logger = Logger.getLogger(TweetsCollector.class.getName());
    static final int MAX_COUNTER = 900;
    private static final int MAX_PAGES = 15;

    @Autowired
    private TweetsService tweetsService;

    private User user;

    @Override
    public void run() {

        Twitter twitter;
        Keys key = null;

        ScheduledUser scheduledUser = scheduledUserService.findOne(SeedsSaver.DATASET_ID, CrawlerScheduler.TWEETS, user.getId());

        if (scheduledUser != null) {
            key = checkKey(key, MAX_COUNTER);
            twitter = connectionService.configureTwitter(key);
            Long userId = scheduledUser.getPk().getUserId();
            logger.log(Level.INFO, "Usuário id {0}, data {1}", new Object[]{userId, new Date()});
            if (!scheduledUser.isCollected()) {
                for (int page = 1; page <= MAX_PAGES; page++) {
                    List<Status> tweets;
                    do {
                        if (key.getCount() >= MAX_COUNTER) {
                            key = checkKey(key, MAX_COUNTER);
                            twitter = connectionService.configureTwitter(key);
                        }
                        try {
                            tweets = twitterService.collectTweets(userId, new Paging(page, 200), twitter);
                            break;
                        } catch (TwitterException e) {
                            key.setCount(MAX_COUNTER);
                            key.setLastUsedAt(new Date());
                        }
                    }while (true);
                    key.setCount(key.getCount() + 1);
                    key.setLastUsedAt(new Date());

                    if (tweets == null || tweets.isEmpty()) break;
                    saveTweets(userId, tweets);
                }
                scheduledUser.setCollected(true);
                scheduledUser.setCollectionDate(new Date());
                scheduledUserService.save(scheduledUser);
            }
        }
    }

    private void saveTweets(Long userId, List<Status> tweets) {
        tweetsService.saveTweets(userId, tweets);
    }

   @Override
    void loadKeys(int maxCount) {
        super.loadKeys(maxCount);
    }

    public void setUser(User user) {
        this.user = user;
    }
}
