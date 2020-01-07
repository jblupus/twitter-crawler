package com.jblupus.twittercrawler.service;

import com.jblupus.twittercrawler.model.Keys;

import java.util.List;

/**
 * Created by joao on 11/30/16.
 */
public interface KeysService {
    List<Keys> loadKeys(int maxCount);
}
