package com.jblupus.twittercrawler.dao;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.jblupus.twittercrawler.model.SeedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by joao on 11/29/16.
 */
@Repository
public class SeedUserDAOImpl implements SeedUserDAO {
    @Autowired
    private CassandraOperations cassandraOperations;


    public void save(SeedUser seedUser) {
        Insert insert = QueryBuilder.insertInto("seed_user");
        insert.setConsistencyLevel(ConsistencyLevel.ONE);
        insert.value("dataset_id", seedUser.getKey().getDatabaseId());
        insert.value("user_id", seedUser.getKey().getUserId());
        insert.value("is_active", seedUser.isActive());
        insert.value("is_scheduled", seedUser.isScheduled());
        cassandraOperations.execute(insert);
    }

    @Override
    public List<SeedUser> findAllByIdBiggerThan(Long datasetId, Long referenceId, int resultLimit) {
        Select select = QueryBuilder.select().from("seed_user");
        select.where(QueryBuilder.eq("dataset_id", datasetId));
        select.where(QueryBuilder.gt("user_id", referenceId));
        select.limit(resultLimit);

        return cassandraOperations.select(select, SeedUser.class);
    }

    @Override
    public SeedUser findOne(long datasetId, long id) {

        Select select = QueryBuilder.select().from("seed_user");
        select.where(QueryBuilder.eq("dataset_id", datasetId));
        select.where(QueryBuilder.eq("user_id", id));

        return cassandraOperations.selectOne(select, SeedUser.class);
    }
}
