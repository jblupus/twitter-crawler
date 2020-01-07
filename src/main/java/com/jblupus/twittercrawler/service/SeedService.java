package com.jblupus.twittercrawler.service;

import com.jblupus.twittercrawler.model.SeedUser;

import java.util.List;

/**
 * Created by joao on 11/29/16.
 */
public interface SeedService {

    List<SeedUser> findAllByIdBiggerThan(Long datasetId, Long referenceId, int resultLimit);

    void save(SeedUser seedUser);

    SeedUser findOne(long datasetId, long id);
}
