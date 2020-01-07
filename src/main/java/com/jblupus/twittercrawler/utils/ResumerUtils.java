package com.jblupus.twittercrawler.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Labtime on 13/03/17.
 */
public class ResumerUtils {
    public static Set<String> loadViableUsers(){
        Set<String> users = new HashSet<>();
        File file;
        if ((file = new File("./files/viableUsers.dat")).exists()) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    users.add(line);
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
        return users;
    }
}
