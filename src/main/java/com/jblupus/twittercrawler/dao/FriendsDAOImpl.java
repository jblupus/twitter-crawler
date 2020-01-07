package com.jblupus.twittercrawler.dao;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.WriteTimeoutException;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.jblupus.twittercrawler.model.Like;
import com.jblupus.twittercrawler.model.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by joao on 12/1/16.
 */
@Repository
public class FriendsDAOImpl implements FriendsDAO {

    @Autowired
    private CassandraOperations cassandraOperations;

    @Override
    public void save(Friend friend) throws WriteTimeoutException, NoHostAvailableException {
        Insert insert = QueryBuilder.insertInto("friend");
        insert.setConsistencyLevel(ConsistencyLevel.ONE);
        insert.value("user_id", friend.getKey().getUserId());
        insert.value("friend_id", friend.getKey().getFriendId());

        cassandraOperations.execute(insert);
    }

    @Override
    public List<Friend> findUserFriends(Long userId, Long baseFriendId) {
        Select select = QueryBuilder.select().from("Friend");
        select.where(QueryBuilder.eq("user_id", userId));
        select.where(QueryBuilder.gt("friend_id", baseFriendId));
        select.limit(300);
        cassandraOperations.select(select, Like.class);

        return cassandraOperations.select(select, Friend.class);
    }
}
