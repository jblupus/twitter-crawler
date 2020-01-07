package com.jblupus.twittercrawler.service;

import com.jblupus.twittercrawler.model.Like;
import org.springframework.stereotype.Service;
import twitter4j.Status;

import java.util.List;

/**
 * Created by joao on 12/1/16.
 */
@Service
public interface LikesService {
    void saveLikes(Long usuarioId, List<Status> likes);
    void save(Like like);
    List<Status> findUserLikes(Long userId);
}
