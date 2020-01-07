package com.jblupus.twittercrawler.service;

import com.jblupus.twittercrawler.model.Keys;
import twitter4j.Twitter;

/**
 * Created by joao on 11/30/16.
 */
public interface ConnectionService {

    Twitter configureTwitter(Keys keys);
}
