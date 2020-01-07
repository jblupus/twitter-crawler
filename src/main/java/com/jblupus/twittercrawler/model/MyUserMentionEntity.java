package com.jblupus.twittercrawler.model;

import twitter4j.UserMentionEntity;

/**
 * Created by joao on 2/3/17.
 */
public class MyUserMentionEntity extends MyTweetEntity implements UserMentionEntity{
    private long id;

    private String name;

    private String screenName;


    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
}
