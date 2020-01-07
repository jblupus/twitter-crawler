import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by joao on 3/11/17.
 */
public final class GenerateViableUsers extends BaseInteractions {
    private static final Logger Log = Logger.getLogger(GenerateViableUsers.class.getName());
    public static void main(String[] args){
        int counter = 0;
        FileWriter fileWriter;
        try {
            String[] filesArray = new File(BaseInteractions.ROOT).list();
            if(filesArray != null) {
                fileWriter = new FileWriter(new File("./files/viableUsers.dat"));
                for (String vu : filesArray) {
                    if ((getFile(vu, RETWEETS_FILENAME).exists() || getFile(vu, LIKES_FILENAME).exists())
                            && getFile(vu, FRIENDS_FILENAME).exists()) {
                        fileWriter.write(vu + "\n");
                        fileWriter.flush();
                    }
                }
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.log(Level.INFO, "Viable Targets: {0}", counter);
    }
}
