package com.jblupus.twittercrawler.service;

import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.ReadTimeoutException;
import com.datastax.driver.core.exceptions.WriteTimeoutException;
import com.jblupus.twittercrawler.dao.SeedUserDAO;
import com.jblupus.twittercrawler.model.SeedUser;
import com.jblupus.twittercrawler.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao on 11/29/16.
 */
@Service
public class SeedServiceImpl implements SeedService {

    @Autowired
    private SeedUserDAO seedUserDAO;

    public List<SeedUser> findAllByIdBiggerThan(Long datasetId, Long referenceId, int resultLimit) {
        List<SeedUser> seeds = seedUserDAO.findAllByIdBiggerThan(datasetId, referenceId, resultLimit);
        return seeds!=null ? seeds : new ArrayList<SeedUser>();
    }

    public void save(SeedUser seedUser) {
        while (true) {
            try {
                seedUserDAO.save(seedUser);
                break;
            } catch (WriteTimeoutException|NoHostAvailableException ex) {
                TimeUtils.sleep(TimeUtils.SLEEP_TIME_MIN);
            }
        }
    }

    @Override
    public SeedUser findOne(long datasetId, long id) {
        while (true) {
            try {
                return seedUserDAO.findOne(datasetId, id);
            } catch (ReadTimeoutException |NoHostAvailableException ex) {
                TimeUtils.sleep(TimeUtils.SLEEP_TIME_MIN);
            }
        }
    }
}
