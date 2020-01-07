package com.jblupus.twittercrawler.utils;

import java.io.File;

/**
 * Created by Labtime on 13/03/17.
 */
public class FileUtils {
    public static boolean mkdirs(String path){
        return mkdirs(new File(path));
    }

    public static boolean mkdirs(File file) {
        return file.mkdirs();
    }

    public static boolean exists(String path) {
        return exists(new File(path));
    }

    public static boolean exists(File file) {
        return file.exists();
    }
}
