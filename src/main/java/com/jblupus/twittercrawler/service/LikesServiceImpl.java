package com.jblupus.twittercrawler.service;

import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.ReadTimeoutException;
import com.datastax.driver.core.exceptions.WriteTimeoutException;
import com.google.gson.Gson;
import com.jblupus.twittercrawler.dao.LikesDAO;
import com.jblupus.twittercrawler.model.Like;
import com.jblupus.twittercrawler.model.TweetPk;
import com.jblupus.twittercrawler.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao on 12/1/16.
 */
@Service
public class LikesServiceImpl implements LikesService {
    @Autowired
    private LikesDAO likesDAO;
    @Autowired
    private Gson gson;

    @Override
    public void saveLikes(Long userId, List<Status> likes) {
        for (Status like : likes) {
            save(new Like(new TweetPk(userId, like.getId()), gson.toJson(like)));
        }
    }

    @Override
    public void save(Like like) {
        while (true) {
            try {
                likesDAO.save(like);
                break;
            } catch (WriteTimeoutException | NoHostAvailableException ex) {
                TimeUtils.sleep(TimeUtils.SLEEP_TIME_MIN);
            }
        }
    }

    @Override
    public List<Status> findUserLikes(Long userId) {
        List<Status> statuses = new ArrayList<>();
        Long baseTwitterId = 0L;
        try {
            while (true) {
                List<Like> likes = findLikes(userId, baseTwitterId);
                if (likes == null || likes.isEmpty()) break;
                for (Like like : likes) {
                    statuses.add(TwitterObjectFactory.createStatus(like.getTweetJson()));
                }
                baseTwitterId = likes.get(likes.size()-1).getKey().getTweet_id();
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return statuses;
    }

    private List<Like> findLikes(Long userId, Long baseTwitterId) {
        while (true) {
            try {
                return likesDAO.findLikes(userId, baseTwitterId);
            } catch (ReadTimeoutException | NoHostAvailableException ex) {
                ex.printStackTrace();
                TimeUtils.sleep(TimeUtils.SLEEP_TIME_MIN);
            }
        }
    }
}
