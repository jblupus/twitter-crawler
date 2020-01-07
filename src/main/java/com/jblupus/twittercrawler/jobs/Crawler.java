package com.jblupus.twittercrawler.jobs;

import com.google.gson.Gson;
import com.jblupus.twitter.crawler.model.*;
import com.jblupus.twittercrawler.model.*;
import inf.ufg.br.jblupus.model.*;
import com.jblupus.twittercrawler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by joao on 12/7/16.
 */
@Component
public class Crawler extends BaseJob implements Runnable{

    private static final Logger logger = Logger.getLogger(CrawlerScheduler.class.getName());

    static final String LIKES = "likes";
    static final String TWEETS = "tweets";
    static final String FRIENDS = "friends";

    @Autowired
    private FriendshipCollector friendshipCollector;

    @Autowired
    private LikesCollector likesCollector;

    @Autowired
    private TweetsCollector tweetsCollector;


    @Autowired
    private UserService userService;

    @Autowired
    private Gson gson;

    public void run() {
        Long refferenceId = 0L;

        loadKeys(MAX_COUNTER);

        friendshipCollector.loadKeys(FriendshipCollector.MAX_COUNTER);
        likesCollector.loadKeys(LikesCollector.MAX_COUNTER);
        tweetsCollector.loadKeys(TweetsCollector.MAX_COUNTER);

        List<SeedUser> seeds;

        Keys key = null;
        Twitter twitter;
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
                    } while (true);
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

        friendshipCollector.setUser(user);

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        friendshipCollector.setUser(user);
        likesCollector.setUser(user);
        tweetsCollector.setUser(user);

        executorService.submit(friendshipCollector);
        executorService.submit(likesCollector);
        executorService.submit(tweetsCollector);

        executorService.shutdown();

        while (!executorService.isTerminated());
        List<Long> friends = friendshipCollector.getFriendsIds();

        chekUsers(friends);
    }

    private void chekUsers(List<Long> friends) {
        if(friends!=null){
            Keys key = null;
            Twitter twitter;
            key = checkKey(key, MAX_COUNTER);
            twitter = connectionService.configureTwitter(key);
            for (Long id : friends) {
                User lUser;
                do {
                    if (key.getCount() >= MAX_COUNTER) {
                        key = checkKey(key, MAX_COUNTER);
                        twitter = connectionService.configureTwitter(key);
                    }
                    try {
                        ScheduledUser scheduledUser = scheduledUserService.findOne(SeedsSaver.DATASET_ID, CrawlerScheduler.TWEETS, id);
                        if (scheduledUser == null) {
                            lUser = twitterService.showUser(id, twitter);
                            if(lUser!=null &&  !lUser.isProtected()) {
                                scheduledUser = new ScheduledUser(new ScheduledUserKey(SeedsSaver.DATASET_ID, lUser.getId(), CrawlerScheduler.TWEETS), false, null);
                                scheduledUserService.save(scheduledUser);
                            } else break;
                        } else break;

                        tweetsCollector.setUser(lUser);
                        tweetsCollector.run();
                        break;
                    } catch (TwitterException e) {
                        key.setCount(MAX_COUNTER);
                        key.setLastUsedAt(new Date());
                    }
                } while (true);
                key.setCount(key.getCount() + 1);
                key.setLastUsedAt(new Date());
            }
        }
    }

    private static final int MAX_COUNTER = 900;
}