package com.jblupus.twittercrawler.dao;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.WriteTimeoutException;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.jblupus.twittercrawler.model.TwitterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;

/**
 * Created by joao on 11/30/16.
 */
@Repository
public class UserDAOImpl implements UserDAO {
    @Autowired
    private CassandraOperations cassandraOperations;


    @Override
    public void save(TwitterUser user) throws WriteTimeoutException, NoHostAvailableException {
        Insert insert = QueryBuilder.insertInto("twitter_user");
        insert.setConsistencyLevel(ConsistencyLevel.ONE);
        insert.value("dataset_id", user.getKey().getDatabaseId());
        insert.value("user_id", user.getKey().getUserId());
        insert.value("user_json", user.getUserJson());
        cassandraOperations.execute(insert);
    }
}
