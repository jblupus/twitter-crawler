package com.jblupus.twittercrawler.service;

import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.ReadTimeoutException;
import com.jblupus.twittercrawler.dao.KeysDAO;
import com.jblupus.twittercrawler.model.OauthToken;
import com.jblupus.twittercrawler.model.Keys;
import com.jblupus.twittercrawler.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao on 11/30/16.
 */
@Service
public class KeysServiceImpl implements KeysService {
    @Autowired
    private KeysDAO keysDAO;
    @Override
    public List<Keys> loadKeys(int maxCount) {
        List<Keys> keys = new ArrayList<>();
        while (true) {
            try {
                List<OauthToken> tokens = keysDAO.loadKeys();
                for(OauthToken token:tokens){
                    keys.add(new Keys(token, maxCount));
                }
                break;
            } catch (ReadTimeoutException |NoHostAvailableException ex) {
                TimeUtils.sleep(TimeUtils.SLEEP_TIME_MIN);
            }
        }
        return keys;
    }
}
