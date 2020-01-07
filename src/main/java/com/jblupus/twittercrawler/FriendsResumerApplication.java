package com.jblupus.twittercrawler;

import com.jblupus.twittercrawler.jobs.FriendsResumer;

import java.io.IOException;

/**
 * Created by Labtime on 15/03/17.
 */
public class FriendsResumerApplication extends Application implements Runnable {
    public static void main(String[] args) {
        new FriendsResumerApplication().run();
    }

    @Override
    public void run() {
        buildContext();

        FriendsResumer r = getContext().getBean(FriendsResumer.class);
        try {
            r.run();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        if (getContext() != null) {
            getContext().destroy();
            getContext().close();
        }
    }
}
