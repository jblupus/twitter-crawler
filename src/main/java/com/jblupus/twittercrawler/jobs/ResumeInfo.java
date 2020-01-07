package com.jblupus.twittercrawler.jobs;

import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by pesquisador on 02/02/17.
 */
@Component
public class ResumeInfo implements Runnable {
    private static final String ROOT = "/media/pesquisador/Seagate Expansion Drive1/Twitter/TwitterCrawler2Resumed/";

    @Override
    public void run() {
        String[] values = new File(ROOT).list();
        long likes = 0;
        long retweets = 0;
        long friends = 0;
        for(String value : values){
            String mRoot = ROOT + value + "/";
            if(new File(mRoot + "friends.dat").exists()) friends ++;
            if(new File(mRoot + "likes.dat").exists()) likes ++;
            if(new File(mRoot + "retweets.dat").exists()) retweets ++;
        }


        System.out.println("Friends: " + friends);
        System.out.println("Likes: " + likes);
        System.out.println("Retweets: " + retweets);
    }
}
