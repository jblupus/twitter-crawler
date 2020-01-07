import org.jetbrains.annotations.Contract;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by joao on 3/11/17.
 */
abstract class BaseInteractions {
    static final String FRIENDS_FILENAME = "friends.dat";
    static final String LIKES_FILENAME = "likes.dat";
    static final String RETWEETS_FILENAME = "retweets.dat";

    private static final String PC1 = "/media/pesquisador/Seagate Expansion Drive1/Twitter/TwitterCrawler2Resumed/";
    private static final String PC2 = "/media/joao/Seagate Expansion Drive1/Twitter/TwitterCrawler2Resumed/";
    static String ROOT;

    static {
        if (new File(PC1).exists()){
            ROOT = PC1;
        } else if ( new File(PC2).exists()){
            ROOT = PC2;
        }
    }



    static Set<String> loadViableUsers(){
        Set<String> viableUsers = new HashSet<>();
        String[] filesArray = new File(ROOT).list();
        if(filesArray != null)
            for (String file : filesArray) {
                if ((getFile(file, RETWEETS_FILENAME).exists() || getFile(file, LIKES_FILENAME).exists())
                        && getFile(file, FRIENDS_FILENAME).exists()) {
                    viableUsers.add(file);
                }
            }
        return viableUsers;
    }

    @Contract("_, _ -> !null")
    static File getFile(String userFile, String interactionFile){
        return new File(ROOT + userFile + "/" + interactionFile);
    }
}
