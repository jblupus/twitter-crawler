import java.io.*;

/**
 * Created by joao on 2/9/17.
 */
public class FriendsLoader {
    private static final String ROOT = "/Users/joao/Projects/R";

    public static void main(String[] args) throws IOException {

        File fileIn = new File(ROOT + "/files/12/friends.dat");
        File fileOut = new File(ROOT + "/files/friends12.txt");

        String line;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileIn));
        FileWriter fileWriter = new FileWriter(fileOut);

        while ((line = bufferedReader.readLine())!=null){
            Long friendID = Long.valueOf(line, 16);
            fileWriter.write(friendID + " ");
        }

        fileWriter.flush();
        fileWriter.close();
        bufferedReader.close();
    }
}
