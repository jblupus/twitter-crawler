package com.jblupus.twittercrawler.service;

import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.ReadTimeoutException;
import com.datastax.driver.core.exceptions.WriteTimeoutException;
import com.google.gson.Gson;
import com.jblupus.twittercrawler.dao.TweetsDAO;
import com.jblupus.twittercrawler.model.MyStatus;
import com.jblupus.twittercrawler.model.Tweet;
import com.jblupus.twittercrawler.model.TweetPk;
import com.jblupus.twittercrawler.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao on 12/1/16.
 */
@Service
public class TweetsServiceImpl implements TweetsService {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TweetsServiceImpl.class.getName());

    @Autowired
    private TweetsDAO tweetsDAO;
    @Autowired
    private Gson gson;

    @Override
    public void saveTweets(Long userId, List<Status> tweets) {
        for (Status status : tweets) {
            save(new Tweet(new TweetPk(userId, status.getId()), gson.toJson(status)));
        }
    }

    @Override
    public void save(Tweet tweet) {
        while (true) {
            try {
                tweetsDAO.save(tweet);
                break;
            } catch (WriteTimeoutException | NoHostAvailableException ex) {
                TimeUtils.sleep(TimeUtils.SLEEP_TIME_MIN);
            }
        }
    }

    @Override
    public List<Status> findUserRetweets(Long userId) {
        List<Status> retweets = new ArrayList<>();
        Long baseTwitterId = 0L;
        try {
            while (true) {
                List<Tweet> tweets = findTweets(userId, baseTwitterId);
                if (tweets == null || tweets.isEmpty()) break;

                for (Tweet tweet : tweets) {
                    Status status = gson.fromJson(tweet.getTweetJson(), MyStatus.class);
                    if (status.isRetweet() || status.getRetweetedStatus() != null) retweets.add(status);
                }
                baseTwitterId = tweets.get(tweets.size() - 1).getKey().getTweet_id();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retweets;
    }

    @Override
    public List<Status> findUserTweets(Long userId) {
        List<Status> statuses = new ArrayList<>();
        Long baseTwitterId = 0L;
        try {
            while (true) {
                List<Tweet> tweets = findTweets(userId, baseTwitterId);
                if (tweets == null || tweets.isEmpty()) break;

                for (Tweet tweet : tweets) {
                    Status status = gson.fromJson(tweet.getTweetJson(), MyStatus.class);
                    statuses.add(status);
                }
                baseTwitterId = tweets.get(tweets.size() - 1).getKey().getTweet_id();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statuses;
    }

    @Override
    public List<Status> findUserMentions(Long userId) {
        List<Status> mentions = new ArrayList<>();
        Long baseTwitterId = 0L;
        try {
            while (true) {
                List<Tweet> tweets = findTweets(userId, baseTwitterId);
                if (tweets == null || tweets.isEmpty()) break;
                for (Tweet tweet : tweets) {
                    Status status = gson.fromJson(tweet.getTweetJson(), MyStatus.class);
                    if (!(status.isRetweet() || status.getRetweetedStatus() != null)
                            && status.getUserMentionEntities() !=null && status.getUserMentionEntities().length > 0) mentions.add(status);
                }
                baseTwitterId = tweets.get(tweets.size() - 1).getKey().getTweet_id();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mentions;
    }

    @Override
    public List<Status> findUserMentions(List<Status> statuses) {
        List<Status> mentions = new ArrayList<>();
        if(statuses!=null)
            statuses.parallelStream()
                    .filter(status -> !(status.isRetweet() || status.getRetweetedStatus() != null)
                            && status.getUserMentionEntities() !=null && status.getUserMentionEntities().length > 0)
                    .forEach(mentions::add);
        /*for (Status status : statuses) {

            if (!(status.isRetweet() || status.getRetweetedStatus() != null)
                    && status.getUserMentionEntities() !=null && status.getUserMentionEntities().length > 0)
                mentions.add(status);
        }*/
        return mentions;
    }

    @Override
    public List<Status> findUserRetweets(List<Status> statuses) {
        List<Status> retweets = new ArrayList<>();
        if(statuses!=null)
            statuses.parallelStream()
                    .filter(status -> status.isRetweet() || status.getRetweetedStatus() != null)
                    .forEach(retweets::add);

        /*for (Status status : statuses) {
            if (status.isRetweet() || status.getRetweetedStatus() != null) retweets.add(status);
        }*/

        return retweets;
    }

    private List<Tweet> findTweets(Long userId, Long baseTweetId) {
        while (true) {
            try {
                return tweetsDAO.findTweets(userId, baseTweetId);
            } catch (ReadTimeoutException | NoHostAvailableException ex) {
                ex.printStackTrace();
                TimeUtils.sleep(TimeUtils.SLEEP_TIME_MIN);
            }
        }
    }
}
