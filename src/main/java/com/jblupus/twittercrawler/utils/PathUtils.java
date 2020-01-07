package com.jblupus.twittercrawler.utils;

import org.jetbrains.annotations.Contract;

import java.io.File;

/**
 * Created by Labtime on 13/03/17.
 */
public class PathUtils {

    private static final String MEDIA_ROOT = new File("/media/pesquisador/Seagate Expansion Drive1/").exists() ? "/media/pesquisador/Seagate Expansion Drive1/" : "/media/joao/Seagate Expansion Drive/";
    private static final String RESUMED_ROOT = "Twitter/TwitterCrawler2Resumed2/";

    @Contract(pure = true)
    public static String getResumedRoot() {
        return MEDIA_ROOT + RESUMED_ROOT;
    }
}
