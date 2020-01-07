package com.jblupus.twittercrawler.jobs;

import com.jblupus.twitter.crawler.service.*;
import com.jblupus.twittercrawler.service.*;
import com.jblupus.twittercrawler.utils.TimeUtils;
import com.jblupus.twittercrawler.model.Keys;
import inf.ufg.br.jblupus.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by joao on 12/1/16.
 */

@Component
class BaseJob {
    protected List<Keys> keys;

    @Autowired
    protected TwitterService twitterService;

    @Autowired
    protected SeedService seedService;

    @Autowired
    protected ConnectionService connectionService;

    @Autowired
    protected ScheduledUserService scheduledUserService;

    @Autowired
    protected KeysService keysService;

    Keys checkKey(Keys key, int maxCount ) {
        if(key == null) return checkKey(Keys.getAvaliableKey(keys), maxCount);
        else if(key.getCount() >= maxCount){
            key.setCount(maxCount);
            key.setLastUsedAt(new Date());
            return Keys.getAvaliableKey(keys);
        }
        return key;
    }

    void loadKeys(int maxCount) {
        keys = keysService.loadKeys(maxCount);
    }

    void sleep() {
        TimeUtils.sleep(TimeUtils.secondToMilliseconds(TimeUtils.minuteToSeconds(15)));
    }
}
