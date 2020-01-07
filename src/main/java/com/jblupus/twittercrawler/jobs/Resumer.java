package com.jblupus.twittercrawler.jobs;

import com.google.gson.Gson;
import com.jblupus.twittercrawler.service.FriendsService;
import com.jblupus.twittercrawler.service.TweetsService;
import com.jblupus.twittercrawler.model.Friend;
import com.jblupus.twittercrawler.service.LikesService;
import com.jblupus.twittercrawler.service.SeedService;
import com.jblupus.twittercrawler.utils.PathUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.UserMentionEntity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Labtime on 15/03/17.
 */
@Component
public class Resumer {

    static final int MAX_COUNTER = 500;

    @Autowired
    protected Gson gson;

    @Autowired
    SeedService seedService;

    @Autowired
    private LikesService likesService;

    @Autowired
    private TweetsService tweetsService;

    @Autowired
    private FriendsService friendsService;

    boolean isResumed(File... files) {
        for (File f : files){
            if (!f.exists()) return false;
        }
        return true;
    }

    List<Friend> handleUserData(Long userId,
                                        boolean seed,
                                        File friendsFile,
                                        File likesFile,
                                        File mentionsFile,
                                        File retweetsFile) throws InterruptedException {
        List<Friend> friends = new ArrayList<>();

        new File(getUserPath(userId)).mkdirs();


        if (!friendsFile.exists() || seed) {
            friends = friendsService.findUserFriends(userId);
            saveFriends(friendsFile, friends).run();
        }
        // i) caso não seja seed -- não seeds não precisam de amigos
        // ou ii) os amigos não são vazios -- seeds precisam de amigos
        if (!seed || !friends.isEmpty()) {
            if (!likesFile.exists()) {
                saveLikes(likesFile, likesService.findUserLikes(userId)).run();
            }

            List<Status> statuses = tweetsService.findUserTweets(userId);

            if (!mentionsFile.exists()) {
                saveMentions(mentionsFile, tweetsService.findUserMentions(statuses)).run();
            }
            if (!retweetsFile.exists()) {
                saveRetweets(retweetsFile, tweetsService.findUserRetweets(statuses)).run();
            }
        }
        return friends;
    }

    @Contract(pure = true)
    private String getUserPath(Long userId){
        return PathUtils.getResumedRoot() + userId + "/";
    }

    @Contract(pure = true)
    String getFriendsPath(Long userId) {
        return getUserPath(userId) + "friends.dat";
    }

    @Contract(pure = true)
    String getLikesPath(Long userId) {
        return getUserPath(userId) + "likes.dat";
    }

    @Contract(pure = true)
    String getRetweetsPath(Long userId) {
        return getUserPath(userId) + "retweets.dat";
    }

    @Contract(pure = true)
    String getMentionsPath(Long userId) {
        return getUserPath(userId) + "mentions.dat";
    }

    @NotNull
    private Saver saveMentions(File file, List<Status> mentions) {
        return () -> {
            if (file !=null && mentions != null && !mentions.isEmpty()) {
                try {
                    FileWriter fileWriter = new FileWriter(file);
                    for (Status mention : mentions) {
                        UserMentionEntity[] mentionEntities = mention.getUserMentionEntities();
                        for (UserMentionEntity entity : mentionEntities) {
                            String hexaTweetId = Long.toHexString(mention.getId());
                            String hexaMentionId = Long.toHexString(entity.getId());
                            String str = "(" + hexaTweetId + ":" + hexaMentionId + ")";
                            fileWriter.write(str + "\n");
                        }
                    }
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @NotNull
    private Saver saveFriends(final File file, final List<Friend> friends) {
        return () -> {
            if (file !=null && friends != null && !friends.isEmpty()) {
                try {
                    FileWriter fileWriter = new FileWriter(file);
                    for (Friend friend : friends) {
                        String str = Long.toHexString(friend.getKey().getFriendId()) + "\n";
                        fileWriter.write(str);
                    }
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @NotNull
    private Saver saveLikes(File file, List<Status> likes) {
        return () -> {
            if (file != null && likes != null && !likes.isEmpty()){
                try {
                    FileWriter fileWriter = new FileWriter(file);
                    for (Status like : likes) {
                        String hexaId = Long.toHexString(like.getUser().getId());
                        String hexaTweetId = Long.toHexString(like.getId());
                        String str = "(" + hexaId + ":" + hexaTweetId + ")";
                        fileWriter.write(str + "\n");
                    }
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @NotNull
    private Saver saveRetweets(File file, List<Status> retweets) {
        return () -> {
            if (file != null && retweets != null && !retweets.isEmpty()) {
                try {
                    FileWriter fileWriter = new FileWriter(file);
                    for (Status retweet : retweets) {
                        String hexaId = Long.toHexString(retweet.getRetweetedStatus().getUser().getId());
                        String hexaTweetId = Long.toHexString(retweet.getRetweetedStatus().getId());
                        String str = "(" + hexaId + ":" + hexaTweetId + ")";
                        fileWriter.write(str + "\n");
                    }
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private interface Saver {
        void run();
    }
}
