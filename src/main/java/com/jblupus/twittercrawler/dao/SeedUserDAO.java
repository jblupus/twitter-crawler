package com.jblupus.twittercrawler.dao;

import com.jblupus.twittercrawler.model.SeedUser;

import java.util.List;

/**
 * Created by joao on 11/29/16.
 */
public interface SeedUserDAO {
    void save(SeedUser seedUser);

    List<SeedUser> findAllByIdBiggerThan(Long datasetId, Long referenceId, int resultLimit);

    SeedUser findOne(long datasetId, long id);
}
