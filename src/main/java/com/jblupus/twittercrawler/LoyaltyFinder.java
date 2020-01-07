package com.jblupus.twittercrawler;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by joao on 2/9/17.
 */
public class LoyaltyFinder implements Runnable {
    private static final int TYPE_LIKES = 0;
    private static final int TYPE_RETWEETS = 1;

    private static final String FIRST_PATTERN = "\\(";
    private static final String SECOND_PATTERN = "\\)";


    private static final String ROOT = "/media/pesquisador/Seagate Expansion Drive1/Twitter/TwitterCrawler2Resumed/";
    private static final String SAVE_ROOT = "/home/pesquisador/Desktop/TwitterLoyalty/";

    public static void main(String[] args) {
        new LoyaltyFinder().run();
    }

    @Override
    public void run() {
        try {
            List<String> users = getUsers();
            for (int i = 0, length = users.size(); i < length && i < 100; i++) {

                String user = users.get(i);

                String userPath = ROOT + user + "/";

                File friendsFile = new File(userPath + "friends.dat");

                if (friendsFile.exists()) {
                    List<String> friends = loadFriends(friendsFile);
                    if (!friends.isEmpty()) {
                        ExecutorService service = Executors.newFixedThreadPool(4);


                        InfoLoader userLikes = new InfoLoader(user, TYPE_LIKES);

                        InfoLoader likes = new InfoLoader(friends, TYPE_LIKES);

                        InfoLoader userRetweets = new InfoLoader(user, TYPE_RETWEETS);
                        InfoLoader retweets = new InfoLoader(friends, TYPE_RETWEETS);

                        service.submit(userLikes);
                        service.submit((userLikes));
                        service.submit((likes));
                        service.submit(retweets);

                        service.shutdown();

                        while (!service.isTerminated()) ;

                        Map<String, Set<Pair>> likesMap = likes.getUserData();
                        Map<String, Set<Pair>> retweetsMap = retweets.getUserData();


                        Set<Pair> userLikesMap = userLikes.getUserData().get(user);
                        Set<Pair> userRetweetsMap = userRetweets.getUserData().get(user);

                        handleUserInteractions(user, "likes", userLikesMap, likesMap, retweetsMap);
                        handleUserInteractions(user, "retweets", userRetweetsMap, likesMap, retweetsMap);

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleUserInteractions(String user, String interaction,
                                        Set<Pair> userInteractions,
                                        Map<String, Set<Pair>> likesMap,
                                        Map<String, Set<Pair>> retweetsMap) throws IOException {

        Map<String, Integer> loyaltyRate = new HashMap<>();
        Map<String, Integer> loyaltyRateForRetweet = new HashMap<>();
        Map<String, Integer> loyaltyRateForLikes = new HashMap<>();

        for (Pair pair : userInteractions) {

            // author com quem usuário interagiu
            String author = pair.getAuthor();

            if (loyaltyRate.containsKey(author)) {
                int interactions = loyaltyRate.get(author) + 1;
                loyaltyRate.put(author, interactions);
            } else {
                loyaltyRate.put(author, 1);
            }

            //tweet de um author com o qual o usuário interagiu

            String tweet = pair.getTweet();
            List<String> friendsWhoInteractedLikes = friendsWhoInteracted(pair, likesMap);
            updateMap(friendsWhoInteractedLikes, loyaltyRateForLikes);

            List<String> friendsWhoInteractedRetweets = friendsWhoInteracted(pair, retweetsMap);
            updateMap(friendsWhoInteractedRetweets, loyaltyRateForRetweet);

        }

        saveLoyalty(user, interaction, "loyalty.csv", loyaltyRate);
        saveLoyalty(user, interaction, "loyalty-likes.csv", mergeLoyalties(loyaltyRate, loyaltyRateForLikes));
        saveLoyalty(user, interaction, "loyalty-retweet.csv", mergeLoyalties(loyaltyRate, loyaltyRateForRetweet));
        saveLoyalty(user, interaction, "loyalty-likes-retweet.csv", mergeLoyalties(loyaltyRate, mergeLoyalties(loyaltyRateForLikes, loyaltyRateForRetweet)));
    }

    private Map<String, Integer> mergeLoyalties(Map<String, Integer> lr1, Map<String, Integer> lr2) {
        if (lr1.size() > lr2.size()) mergeLoyalties(lr2, lr1);
        for (Map.Entry<String, Integer> entry : lr1.entrySet()) {
            String key = entry.getKey();
            if (lr2.containsKey(key)) {
                int value = lr2.get(key) + entry.getValue();
                lr2.put(key, value);
            } else {
                lr2.put(key, entry.getValue());
            }
        }
        return lr2;
    }

    private void saveLoyalty(String user, String interaction, String loyalty, Map<String, Integer> loyaltyRate) throws IOException {

        String filePath = SAVE_ROOT + interaction + "/" + user + "/";

        new File(filePath).mkdirs();

        FileWriter fileWriter = new FileWriter(new File(filePath + loyalty));

        for (Map.Entry<String, Integer> entry : loyaltyRate.entrySet()) {
            String line = Long.toHexString(Long.parseLong(entry.getKey())) + " " + entry.getValue() + "\n";
            fileWriter.write(line);
        }

        fileWriter.flush();
        fileWriter.close();
    }

    private void updateMap(List<String> friendsWhoInteracted, Map<String, Integer> loyaltyRate) {
        for (String friend : friendsWhoInteracted) {
            if (loyaltyRate.containsKey(friend)) {
                int interactions = loyaltyRate.get(friend) + 1;
                loyaltyRate.put(friend, interactions);
            } else {
                loyaltyRate.put(friend, 1);
            }
        }
    }

    private List<String> friendsWhoInteracted(Pair pair, Map<String, Set<Pair>> friendsAndInteractions) {
        List<String> friendsWhoInteracted = new ArrayList<>();
        for (Map.Entry<String, Set<Pair>> entry : friendsAndInteractions.entrySet()) {
            //checa se o usuário também interagiu com um <<author>>
            // e se checa se a intereção foi com o mesmo <<tweet>>
            if (entry.getValue().contains(pair)) {
                friendsWhoInteracted.add(entry.getKey());
            }
        }
        return friendsWhoInteracted;
    }

    private List<String> loadFriends(File friendsFile) throws IOException {
        List<String> friends = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(friendsFile));

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Long friendID = Long.valueOf(line, 16);
            friends.add(String.valueOf(friendID));
        }
        return friends;
    }

    public List<String> getUsers() {
        String[] usersDirs = new File(ROOT).list();
        List<String> users = Arrays.asList(usersDirs);
        Collections.sort(users);
        return users;
    }

    class InfoLoader implements Runnable {

        private final List<String> users;
        private final int type;

        private Map<String, Set<Pair>> userData;

        InfoLoader(List<String> users, int type) {
            this.users = users;
            this.type = type;
        }

        InfoLoader(String user, int type) {
            this.users = new ArrayList<>();
            this.users.add(user);
            this.type = type;
        }

        @Override
        public void run() {
            userData = new HashMap<>();

            try {
                for (String user : users) {

                    if (!userData.containsKey(user)) userData.put(user, new HashSet<Pair>());
                    String userPath = ROOT + user + "/";
                    if (new File(userPath).exists()) {
                        switch (type) {
                            case TYPE_LIKES:
                                loadLikes(user, userPath);
                                break;
                            case TYPE_RETWEETS:
                                loadRetweets(user, userPath);
                                break;

                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                userData = null;
            }
        }

        private void loadRetweets(String user, String userPath) throws IOException {
            loadInfo(user, userPath + "retweets.dat");
        }

        private void loadLikes(String user, String userPath) throws IOException {
            loadInfo(user, userPath + "likes.dat");
        }

        public Map<String, Set<Pair>> getUserData() {
            return userData;
        }

        private void loadInfo(String user, String filePath) throws IOException {
            List<String> friends = new ArrayList<>();
            File file = new File(filePath);
            if (file.exists()) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] splits = line.split(":");
                    String author = splits[0].split(FIRST_PATTERN)[1];
                    String tweet = splits[1].split(SECOND_PATTERN)[0];
                    Set<Pair> set = userData.get(user);
                    Pair pair = new Pair(tweet, author);
                    set.add(pair);
                }
            }
        }
    }

    class UserData {
        private final Map<String, Set<String>> retweetsMap = new HashMap<>();
        private final Map<String, Set<String>> likesMap = new HashMap<>();

        public Map<String, Set<String>> getRetweetsMap() {
            return retweetsMap;
        }

        public Map<String, Set<String>> getLikesMap() {
            return likesMap;
        }
    }

    class Pair {
        private String tweet, author;

        public Pair(String tweet, String author) {
            this.tweet = tweet;
            this.author = author;
        }

        public String getTweet() {
            return tweet;
        }

        public void setTweet(String tweet) {
            this.tweet = tweet;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair pair = (Pair) o;

            return tweet.equals(pair.tweet);
        }

        @Override
        public int hashCode() {
            return tweet.hashCode();
        }
    }
}
