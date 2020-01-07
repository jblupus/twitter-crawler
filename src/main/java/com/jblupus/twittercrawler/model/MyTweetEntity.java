package com.jblupus.twittercrawler.model;

import twitter4j.TweetEntity;

/**
 * Created by joao on 2/3/17.
 */
public class MyTweetEntity implements TweetEntity{
    private int start;
    private int end;
    private String text;

    @Override
    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
