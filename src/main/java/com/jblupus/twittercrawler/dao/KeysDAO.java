package com.jblupus.twittercrawler.dao;

import com.jblupus.twittercrawler.model.OauthToken;

import java.util.List;

/**
 * Created by joao on 11/30/16.
 */
public interface KeysDAO {
    List<OauthToken> loadKeys();
}
