package com.jblupus.twittercrawler.jobs;

import com.jblupus.twittercrawler.model.SeedUser;
import com.jblupus.twittercrawler.utils.PathUtils;
import com.jblupus.twittercrawler.utils.FileUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Labtime on 15/03/17.
 */
@Component
public class FriendsResumer extends Resumer {
    public void run() throws InterruptedException, IOException {
        if (FileUtils.exists(PathUtils.getResumedRoot()) || FileUtils.mkdirs(PathUtils.getResumedRoot())) {
            Long refferenceId = 0L;
            List<SeedUser> seeds;
            int countSeeds = 0;
            while (!(seeds = seedService.findAllByIdBiggerThan(SeedsSaver.DATASET_ID, refferenceId, MAX_COUNTER)).isEmpty()) {
                for (SeedUser seedUser : seeds) {
                    Long userId = seedUser.getKey().getUserId();
                    File friendsFile = new File(getFriendsPath(userId));
                    Set<Long> friends;
                    if (friendsFile.exists() && (friends = loadFriends(friendsFile)) !=null) {
                        for (Long friend : friends) {
                            friendsFile = new File(getFriendsPath(friend));
                            File likesFile = new File(getLikesPath(friend));
                            File  mentionsFile = new File(getMentionsPath(friend));
                            File  retweetsFile = new File(getRetweetsPath(friend));
                            if(!isResumed(likesFile, mentionsFile, retweetsFile))
                            handleUserData(friend,
                                    false,
                                    friendsFile,
                                    likesFile,
                                    mentionsFile,
                                    retweetsFile);
                        }
                        refferenceId = userId;
                        System.out.println("Seeds: " + ++countSeeds);
                    }
                }
            }
        }
    }

    private Set<Long> loadFriends(File friendsFile) throws IOException {
        Set<Long> friends = new HashSet<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(friendsFile));
        String line;
        while ((line = bufferedReader.readLine())!=null){
            friends.add(Long.parseLong(line, 16));
        }
        bufferedReader.close();
        return friends;
    }
}
