package com.jblupus.twittercrawler.jobs;

import com.google.gson.Gson;
import com.jblupus.twitter.crawler.model.*;
import com.jblupus.twittercrawler.model.*;
import com.jblupus.twittercrawler.service.UserService;
import inf.ufg.br.jblupus.model.*;
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
 * Created by joao on 11/30/16.
 */
@Component
public class CrawlerScheduler extends BaseJob implements Runnable {
    private static final Logger logger = Logger.getLogger(CrawlerScheduler.class.getName());
    public static final String LIKES = "likes";
    public static final String TWEETS = "tweets";
    public static final String FRIENDS = "friends";
    @Autowired
    private UserService userService;

    @Autowired
    private Gson gson;

    public void run() {
        Long refferenceId = 0L;

        loadKeys(MAX_COUNTER);

        List<SeedUser> seeds;
        Keys key = null;
        Twitter twitter = null;
        while (!(seeds = seedService.findAllByIdBiggerThan(SeedsSaver.DATASET_ID, refferenceId, MAX_COUNTER)).isEmpty()) {
            key = checkKey(key, MAX_COUNTER);
            twitter = connectionService.configureTwitter(key);
            for (SeedUser seedUser : seeds) {
                Long userId = seedUser.getKey().getUserId();
                if (seedUser.isActive() && !seedUser.isScheduled()) {
                    User user;
                    do {
                        if (key.getCount() >= MAX_COUNTER) {
                            key = checkKey(key, MAX_COUNTER);
                            twitter = connectionService.configureTwitter(key);
                        }
                        try {
                            user = twitterService.showUser(userId, twitter);
                            break;
                        } catch (TwitterException e) {
                            key.setCount(MAX_COUNTER);
                            key.setLastUsedAt(new Date());
                        }
                    }while (true);
                    key.setCount(key.getCount() + 1);
                    key.setLastUsedAt(new Date());
                    if (user != null && !user.isProtected()) {
                        scheduleUser(user);
                        seedUser.setActive(true);
                        seedUser.setScheduled(true);
                        logger.log(Level.INFO, "Usuário id {0}, data {1}", new Object[]{userId, new Date()});
                    } else {
                        seedUser.setActive(false);
                        seedUser.setScheduled(false);
                    }
                    seedService.save(seedUser);
                }
                refferenceId = userId;
            }
        }
    }

    private void scheduleUser(User user) {
        scheduledUserService.save(new ScheduledUser(new ScheduledUserKey(SeedsSaver.DATASET_ID, user.getId(), LIKES), false, null));
        scheduledUserService.save(new ScheduledUser(new ScheduledUserKey(SeedsSaver.DATASET_ID, user.getId(), TWEETS), false, null));
        scheduledUserService.save(new ScheduledUser(new ScheduledUserKey(SeedsSaver.DATASET_ID, user.getId(), FRIENDS), false, null));
        userService.save(new TwitterUser(new TwitterUserKey(SeedsSaver.DATASET_ID, user.getId()), gson.toJson(user)));
    }

    private static final int MAX_COUNTER = 900;

}
