package com.jblupus.twittercrawler.service;

import com.jblupus.twittercrawler.model.Keys;
import org.springframework.stereotype.Service;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by joao on 11/30/16.
 */
@Service
public class ConnectionServiceImpl implements ConnectionService {

    @Override
    public Twitter configureTwitter(Keys keys) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(keys.getApiKey())
                .setOAuthConsumerSecret(keys.getApiSecret())
                .setOAuthAccessToken(keys.getToken())
                .setOAuthAccessTokenSecret(keys.getTokenSecret())
                .setHttpConnectionTimeout(100000);

        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
        return twitterFactory.getInstance();
    }
}
