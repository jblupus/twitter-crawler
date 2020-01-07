package com.jblupus.twittercrawler.dao;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.jblupus.twittercrawler.model.OauthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by joao on 11/30/16.
 */
@Repository
public class KeysDAOImpl implements KeysDAO {
    @Autowired
    private CassandraOperations cassandraOperations;

    @Override
    public List<OauthToken> loadKeys() {
        Select select = QueryBuilder.select().from("oauth_token");

        return cassandraOperations.select(select, OauthToken.class);
    }
}
