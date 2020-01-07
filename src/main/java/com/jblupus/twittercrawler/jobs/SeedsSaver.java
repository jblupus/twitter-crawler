package com.jblupus.twittercrawler.jobs;

import com.jblupus.twittercrawler.model.SeedUser;
import com.jblupus.twittercrawler.model.SeedUserKey;
import com.jblupus.twittercrawler.service.SeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by joao on 11/30/16.
 */
@Component
public class SeedsSaver implements Runnable {
    static final Long DATASET_ID = 2L;
    private static final Logger logger = Logger.getLogger(SeedsSaver.class.getName());
    @Autowired
    private SeedService seedService;

    public void run() {
        try {
            saveSeeds("files/1.ls");
            logger.log(Level.INFO, "Nome Arquivo {0} ", new Object[]{"1.ls"});
            saveSeeds("files/2.ls");
            logger.log(Level.INFO, "Nome Arquivo {0} ", new Object[]{"2.ls"});
            saveSeeds("files/3.ls");
            logger.log(Level.INFO, "Nome Arquivo {0} ", new Object[]{"3.ls"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param filename
     * @throws IOException
     */
    private void saveSeeds(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line;

        while ((line = bufferedReader.readLine())!=null){
            Long userId = Long.parseLong(line, 16);
            seedService.save(new SeedUser(new SeedUserKey(DATASET_ID, userId), true, false));
        }
    }
}
