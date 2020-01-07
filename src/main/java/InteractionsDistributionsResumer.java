import com.jblupus.twittercrawler.utils.ResumerUtils;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by joao on 3/11/17.
 */
final class InteractionsDistributionsResumer extends BaseInteractions implements Runnable{
    private static final Logger Log = Logger.getLogger(InteractionsDistributionsResumer.class.getName());

    @Override
    public void run() {
        Log.log(Level.INFO, ROOT);
        final Set<String> targetFiles = ResumerUtils.loadViableUsers();
        Log.log(Level.INFO, "Initial number of files {0}", targetFiles.size());
        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(new File("./files/" + LIKES_FILENAME));
            for(String targetFile : targetFiles) {
                final Set<String> friends = getFriends(targetFile);
                if (getFile(targetFile, FRIENDS_FILENAME).exists()
                        && getFile(targetFile, LIKES_FILENAME).exists()) {
                    handleUser(fileWriter, targetFile, LIKES_FILENAME, friends);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileWriter != null){
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        fileWriter = null;
        try{
            fileWriter = new FileWriter(new File("./files/" + RETWEETS_FILENAME));
            for(String targetFile : targetFiles){
                final Set<String> friends = getFriends(targetFile);
                if(getFile(targetFile, FRIENDS_FILENAME).exists() && getFile(targetFile, RETWEETS_FILENAME).exists())
                    handleUser(fileWriter, targetFile, RETWEETS_FILENAME, friends);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileWriter != null){
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void handleUser(FileWriter fileWriter, String userFile, String filename, Set<String> friends) throws IOException {

        final Map<String, Set<String>> friendsInteractions = getFriendsInteractions(friends);
        final Map<String, Integer> counters = countInteractions(friends, userFile, filename, friendsInteractions);
        List<Integer> counts = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : counters.entrySet()){
            counts.add(entry.getValue());
        }
        Collections.sort(counts);
        fileWriter.write(userFile);
        for (int it = counts.size(); it >0;){
            fileWriter.write(" " + counts.get(--it));
        }
        fileWriter.write("\n");
        fileWriter.flush();
    }

    private static Map<String, Set<String>> getFriendsInteractions(Set<String> friends) {
        Map<String, Set<String>> friendsInteractions = new HashMap<>();
        friendsInteractions(friendsInteractions, friends, LIKES_FILENAME);
        friendsInteractions(friendsInteractions, friends, RETWEETS_FILENAME);
        return friendsInteractions;
    }

    private static void friendsInteractions(Map<String, Set<String>> friendsInteractions, Set<String> friends, String filename) {
        for (String userFile: friends){
            File file;
            if ((file = getFile(userFile, filename)).exists()) {
                BufferedReader bufferedReader = null;
                try {
                    bufferedReader = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        final String[] strs = cleanString(line).split(":");
                        if(friendsInteractions.containsKey(userFile)){
                            friendsInteractions.get(userFile).add(strs[1]);
                        } else {
                            Set<String> set = new HashSet<>();
                            set.add(strs[1]);
                            friendsInteractions.put(userFile, set);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private static Set<String> getFriends(String userFile) {
        Set<String> friends = new HashSet<>();
        File file;
        if ((file = getFile(userFile, FRIENDS_FILENAME)).exists()) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    friends.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(bufferedReader!=null){
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return friends;
    }

    private static void incrementCounter(Map<String, Integer> sortedMap, String sortedKey){
        int incrementValue = 1;
        if (sortedMap.containsKey(sortedKey)){
            incrementValue += sortedMap.get(sortedKey);
            sortedMap.put(sortedKey, incrementValue);
        } else {
            sortedMap.put(sortedKey, incrementValue);
        }
    }

    private static Map<String, Integer> countInteractions(Set<String> friends, String userFile, final String filename, Map<String, Set<String>> friendsInteractions){
        Map<String, Integer> sortedMap = new HashMap<>();
        File file;
        if ((file = getFile(userFile, filename)).exists()) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    final String[] strs = cleanString(line).split(":");
                    incrementCounter(sortedMap, strs[0]);
                    friends.parallelStream()
                            .filter(friend -> hasFriendInteraction(friend, friendsInteractions, strs[1]))
                            .forEach(friend-> incrementCounter(sortedMap, friend));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(bufferedReader!=null){
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sortedMap;
    }

    private static boolean hasFriendInteraction(String friendFile, Map<String, Set<String>> interactionsMap, String item) {
        return interactionsMap.containsKey(friendFile) && interactionsMap.get(friendFile).contains(item);
    }

    private static String cleanString(String string){
        return string.replaceAll("[^a:-zZ-Z1-9 ]", "");
    }

    public static void main(String[] args){
        new Thread(new InteractionsDistributionsResumer()).start();
    }
}
