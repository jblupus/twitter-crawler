package com.jblupus.twittercrawler.dao;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.WriteTimeoutException;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.jblupus.twittercrawler.model.ScheduledUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by joao on 11/30/16.
 */
@Repository
public class ScheduledUserDAOImpl implements ScheduledUserDAO {
    @Autowired
    private CassandraOperations cassandraOperations;

    @Override
    public void save(ScheduledUser scheduledUser) throws WriteTimeoutException, NoHostAvailableException {
        Insert insert = QueryBuilder.insertInto("scheduled_user");
        insert.setConsistencyLevel(ConsistencyLevel.ONE);
        insert.value("dataset_id", scheduledUser.getPk().getDatasetId());
        insert.value("user_id", scheduledUser.getPk().getUserId());
        insert.value("information", scheduledUser.getPk().getInformation());
        insert.value("is_collected", scheduledUser.isCollected());
        insert.value("collection_date", scheduledUser.getCollectionDate());

        cassandraOperations.execute(insert);
    }

    @Override
    public List<ScheduledUser> findById(Long datasetId, String information, Long referenceId, int limit) {
        Select select = QueryBuilder.select().from("scheduled_user");
        select.where(QueryBuilder.eq("dataset_id", datasetId));
        select.where(QueryBuilder.eq("information", information));
        select.where(QueryBuilder.gte("user_id", referenceId));
        select.limit(limit);

        return cassandraOperations.select(select, ScheduledUser.class);
    }

    @Override
    public ScheduledUser findOnde(Long datasetId, String information,long id) {
        Select select = QueryBuilder.select().from("scheduled_user");
        select.where(QueryBuilder.eq("dataset_id", datasetId));
        select.where(QueryBuilder.eq("information", information));
        select.where(QueryBuilder.eq("user_id", id));

        return cassandraOperations.selectOne(select, ScheduledUser.class);
    }
}
