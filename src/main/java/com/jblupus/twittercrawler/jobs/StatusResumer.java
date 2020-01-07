package com.jblupus.twittercrawler.jobs;

import com.jblupus.twittercrawler.service.TweetsService;
import com.jblupus.twittercrawler.utils.StringUtils;
import com.jblupus.twittercrawler.model.SeedUser;
import com.jblupus.twittercrawler.service.SeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.Status;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by joao on 2/14/17.
 */
@Component
public class StatusResumer implements Runnable {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(StatusResumer.class.getName());
    private static String ROOT;

    static {
        if (new File("/media/pesquisador/Seagate Expansion Drive1/Twitter/TwitterCrawler2StatusResumed/").exists()){
            ROOT = "/media/pesquisador/Seagate Expansion Drive1/Twitter/TwitterCrawler2StatusResumed/";
        } else if ( new File("/media/joao/Seagate Expansion Drive1/Twitter/TwitterCrawler2StatusResumed/").exists()){
            ROOT ="/media/joao/Seagate Expansion Drive1/Twitter/TwitterCrawler2StatusResumed/";
        }
    }


    @Autowired
    private SeedService seedService;

    @Autowired
    private TweetsService tweetsService;

    private static final int MAX_COUNTER = 500;

    public void run() {

        new File(ROOT).mkdirs();

        Long refferenceId = 0L;
        List<SeedUser> seeds;
        while (!(seeds = seedService.findAllByIdBiggerThan(SeedsSaver.DATASET_ID, refferenceId, MAX_COUNTER)).isEmpty()) {
            for (SeedUser seedUser : seeds) {
                Long userId = seedUser.getKey().getUserId();
                String filepath = ROOT + userId;
                File file = new File(filepath);
                if(!handleUserData(userId)){
                    file.delete();
                }
                refferenceId = userId;

            }
        }
    }

    private boolean handleUserData(Long userId) {
        List<Status> statuses = tweetsService.findUserTweets(userId);
        String filepath = ROOT + userId;
        File file = new File(filepath + "tweets.txt");
        if (statuses != null && !statuses.isEmpty()) {
            try {
                FileWriter fileWriter = new FileWriter(file);
                for (Status status : statuses){
                    fileWriter.write(StringUtils.toLowerCase(StringUtils.unspecial(StringUtils.unaccent(status.getText())))+"\n");
                }

                fileWriter.flush();
                fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
