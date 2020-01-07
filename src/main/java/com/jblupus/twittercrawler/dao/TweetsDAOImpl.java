package com.jblupus.twittercrawler.dao;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.WriteTimeoutException;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.jblupus.twittercrawler.model.Like;
import com.jblupus.twittercrawler.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by joao on 12/1/16.
 */
@Repository
public class TweetsDAOImpl implements TweetsDAO {
    @Autowired
    private CassandraOperations cassandraOperations;

    @Override
    public void save(Tweet tweet) throws WriteTimeoutException, NoHostAvailableException {
        Insert insert = QueryBuilder.insertInto("tweet");
        insert.setConsistencyLevel(ConsistencyLevel.ONE);

        insert.value("user_id", tweet.getKey().getUserId());
        insert.value("tweet_id", tweet.getKey().getTweet_id());
        insert.value("tweet_json", tweet.getTweetJson());

        cassandraOperations.execute(insert);
    }

    @Override
    public List<Tweet> findTweets(Long userId, long baseTweetId) {
        Select select = QueryBuilder.select().from("Tweet");
        select.where(QueryBuilder.eq("user_id", userId));
        select.where(QueryBuilder.gt("tweet_id", baseTweetId));
        select.limit(300);
        cassandraOperations.select(select, Like.class);

        return cassandraOperations.select(select, Tweet.class);
    }
}
