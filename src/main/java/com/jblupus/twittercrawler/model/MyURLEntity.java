package com.jblupus.twittercrawler.model;

import twitter4j.URLEntity;

/**
 * Created by joao on 2/3/17.
 */
public class MyURLEntity implements URLEntity{
    private int end;
    private String text;
    private String url;
    private String displayURL;
    private int start;
    private String expandedURL;

    public void setEnd(int end) {
        this.end = end;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDisplayURL(String displayURL) {
        this.displayURL = displayURL;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setExpandedURL(String expandedURL) {
        this.expandedURL = expandedURL;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public String getExpandedURL() {
        return expandedURL;
    }

    @Override
    public String getDisplayURL() {
        return displayURL;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public int getEnd() {
        return end;
    }
}
