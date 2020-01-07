package com.jblupus.twittercrawler.dao;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.WriteTimeoutException;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.jblupus.twittercrawler.model.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by joao on 12/1/16.
 */
@Repository
public class LikesDAOImpl implements LikesDAO {
    @Autowired
    private CassandraOperations cassandraOperations;

    @Override
    public void save(Like like) throws WriteTimeoutException, NoHostAvailableException {
        Insert insert = QueryBuilder.insertInto("like");
        insert.setConsistencyLevel(ConsistencyLevel.ONE);

        insert.value("user_id", like.getKey().getUserId());
        insert.value("tweet_id", like.getKey().getTweet_id());
        insert.value("tweet_json", like.getTweetJson());

        cassandraOperations.execute(insert);
    }

    @Override
    public List<Like> findLikes(Long userId, Long baseTwitterId) {

        Select select = QueryBuilder.select().from("like");
        select.where(QueryBuilder.eq("user_id", userId));
        select.where(QueryBuilder.gt("tweet_id", baseTwitterId));
        select.limit(300);
        return cassandraOperations.select(select, Like.class);
    }
}
