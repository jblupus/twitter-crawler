package com.jblupus.twittercrawler.model;

import com.jblupus.twittercrawler.utils.TimeUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by joao on 11/30/16.
 */
public class Keys implements Comparable<Keys> {

    private static final Logger logger = Logger.getLogger(Keys.class.getName());

    private final OauthToken tokens;

    private int count;

    private final int maxCount;

    private Date lastUsedAt;

    public Keys(OauthToken tokens, int maxCount){
        super();
        this.tokens = tokens;
        this.count = 0;
        this.lastUsedAt = new Date(0);
        this.maxCount = maxCount;
    }

    public Long getId() {
        return tokens.getId();
    }

    public String getApiKey() {
        return tokens.getApiKey();
    }

    public String getApiSecret() {
        return tokens.getApiSecret();
    }

    public String getToken() {
        return tokens.getToken();
    }

    public String getTokenSecret() {
        return tokens.getTokenSecret();
    }

    public final int getCount() {
        return count;
    }

    public final void setCount(int count) {
        this.count = count;
    }

    public final Date getLastUsedAt() {
        return lastUsedAt;
    }

    public final void setLastUsedAt(Date lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    public final int getMaxCount() {
        return maxCount;
    }

    public OauthToken getTokens() {
        return tokens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Keys keys = (Keys) o;

        if (count != keys.count) return false;
        if (maxCount != keys.maxCount) return false;
        if (!tokens.equals(keys.tokens)) return false;
        return lastUsedAt.equals(keys.lastUsedAt);

    }

    @Override
    public int hashCode() {
        int result = tokens.hashCode();
        result = 31 * result + count;
        result = 31 * result + maxCount;
        result = 31 * result + lastUsedAt.hashCode();
        return result;
    }

    @Override
    public int compareTo(Keys keys) {
//		return Integer.valueOf(hashCode()).compareTo(Integer.valueOf(rateLimitKey.hashCode()));
//		return getLastUsedAt().after(rateLimitKey.getLastUsedAt()) ? 1 : -1;
        return Integer.valueOf(this.count).compareTo(keys.count);
    }

    @Override
    public String toString() {
        return "RateLimitKey [tokens=" + tokens + ", count=" + count
                + ", maxCount=" + maxCount + ", lastUsedAt=" + lastUsedAt + "]";
    }


    public static Keys getAvaliableKey(List<Keys> keys) {
        Date now = new Date(System.currentTimeMillis());

        for(Keys key:keys){
            logger.log(Level.INFO, "Key info: id={0}, last used at {1}, used {2} times.", new Object[]{key.getId(), key.getLastUsedAt(), key.getCount()});
            if(TimeUtils.handleSleepTime(key.getLastUsedAt().getTime(), now.getTime())==0){
                key.setCount(0);
            }
        }

        Collections.sort(keys);

        if(keys.get(0).getCount() >= keys.get(0).getMaxCount()){
            keys.get(0).setCount(keys.get(0).getCount() % keys.get(0).getMaxCount());
            try {
                long time = TimeUtils.handleSleepTime(keys.get(0).getLastUsedAt().getTime(), now.getTime());
                logger.log(Level.INFO, "Going to sleep {0} miliseconds", new Object[]{time});
                Thread.sleep(time);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            return getAvaliableKey(keys);
        }

        Keys key = keys.get(0);

        logger.log(Level.INFO, "Selected Key id={0}, last used at {1}, used {2} times.", new Object[]{key.getId(), key.getLastUsedAt(), key.getCount()});
        return keys.get(0);
    }
}
