package com.jblupus.twittercrawler.jobs;

import com.jblupus.twittercrawler.model.SeedUser;
import com.jblupus.twittercrawler.model.Friend;
import com.jblupus.twittercrawler.utils.FileUtils;
import com.jblupus.twittercrawler.utils.PathUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * Created by joao on 1/30/17.
 */
@Component
public class SeedsResumer extends Resumer {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SeedsResumer.class.getName());
    public void run() throws InterruptedException {
        if (FileUtils.exists(PathUtils.getResumedRoot()) || FileUtils.mkdirs(PathUtils.getResumedRoot())) {
            /*Set<String> targetUsers = ResumerUtils.loadViableUsers();
            for (String targetUser : targetUsers) {
                Long userId = Long.parseLong(targetUser, 16);
                List<Friend> friends = handleUserData(userId);
                for (Friend friend : friends) {
                    handleUserData(friend.getKey().getFriendId());
                }
            }*/
            Long refferenceId = 0L;
            List<SeedUser> seeds;
            int countSeeds = 0;
            while (!(seeds = seedService.findAllByIdBiggerThan(SeedsSaver.DATASET_ID, refferenceId, MAX_COUNTER)).isEmpty()) {
                for (SeedUser seedUser : seeds) {
                    Long userId = seedUser.getKey().getUserId();

                    File friendsFile, likesFile, mentionsFile, retweetsFile;

                    friendsFile = new File(getFriendsPath(userId));
                    likesFile = new File(getLikesPath(userId));
                    mentionsFile = new File(getMentionsPath(userId));
                    retweetsFile = new File(getRetweetsPath(userId));

                    if(!isResumed(friendsFile, likesFile, mentionsFile, retweetsFile)) {

                        List<Friend> friends = handleUserData(userId,
                                true,
                                friendsFile,
                                likesFile,
                                mentionsFile,
                                retweetsFile);

                  /*      for (Friend friend : friends) {
                            Long friendId = friend.getKey().getFriendId();
                            friendsFile = new File(getFriendsPath(friendId));
                            likesFile = new File(getLikesPath(friendId));
                            mentionsFile = new File(getMentionsPath(friendId));
                            retweetsFile = new File(getRetweetsPath(friendId));

                            handleUserData(friendId,
                                    false,
                                    friendsFile,
                                    likesFile,
                                    mentionsFile,
                                    retweetsFile);
                        }*/
                    }
                    refferenceId = userId;
                    System.out.println("Seeds: " + ++countSeeds);
                }
            }
        }

        /*new File(PathUtils.RESUMED_ROOT).mkdirs();
        Long refferenceId = 0L;
        List<SeedUser> seeds;
        while (!(seeds = seedService.findAllByIdBiggerThan(SeedsSaver.DATASET_ID, refferenceId, MAX_COUNTER)).isEmpty()) {
            for (SeedUser seedUser : seeds) {
                Long userId = seedUser.getKey().getUserId();

                List<Friend> friends = handleUserData(userId);

                for (Friend friend : friends) {
                    handleUserData(friend.getKey().getFriendId());
                }

                refferenceId = userId;
            }
        }*/
    }


}
