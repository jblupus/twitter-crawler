package com.jblupus.twittercrawler;

import com.jblupus.twittercrawler.jobs.SeedsResumer;

/**
 * Created by Labtime on 13/03/17.
 */
public class SeedsResumerApplication extends Application implements Runnable {
    public static void main(String[] args) {
        new SeedsResumerApplication().run();
    }

    @Override
    public void run() {
        buildContext();

        SeedsResumer r = getContext().getBean(SeedsResumer.class);
        try {
            r.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (getContext() != null) {
            getContext().destroy();
            getContext().close();
        }
    }
}
