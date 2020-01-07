package com.jblupus.twittercrawler.utils;

import java.util.Date;

/**
 * Created by joao on 11/30/16.
 */
public class TimeUtils {
    public static final long SLEEP_TIME_MIN = 1000;
    private static final long MILI_SECONDS = 1;
    private static final long SECOND = 1000 * MILI_SECONDS;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;
    private static final long MONTH = 30 * DAY;

    public static long handleSleepTime(long time1, long time2) {
        long baseTime = secondToMilliseconds(minuteToSeconds(15));
        if( Math.abs(time1 - time2) > baseTime ){
            return 0;
        }
        return Math.abs(baseTime - Math.abs(time1 - time2));
    }

    public static long secondToMilliseconds(long second) {
        return second * 1000;
    }

    public static long minuteToSeconds(long minute) {
        return (minute * 60);
    }

    public static long hourToMinutes(long time) {
        return (time * 60);
    }

    public static Date getMonthsBeforeDate(Date referentialDate, int numberOfMonths) {
        return new Date(referentialDate.getTime() - (numberOfMonths * MONTH));
    }

    public static  Date getYearZero() {
        return new Date((long)0);
    }

    public static long minuteToMilliseconds(long minute) {
        return secondToMilliseconds(minuteToSeconds(minute));
    }

    public static void sleep(long l) {
        while (true) {
            try {
                Thread.sleep(l);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    /*public static Date handleReferentialDate(Tweet favorite, Tweet tweet) {
        if(favorite !=null && tweet !=null){
            return favorite.getCreatedAt().after(tweet.getCreatedAt()) ? new Date(favorite.getCreatedAt().getTime() + 1000) : new Date(tweet.getCreatedAt().getTime() + 1000);
        } else if(favorite != null){
            return new Date(favorite.getCreatedAt().getTime() + 1000);
        }else if(tweet !=null){
            return new Date(tweet.getCreatedAt().getTime() + 1000);
        }
        return null;
    }*/
}
