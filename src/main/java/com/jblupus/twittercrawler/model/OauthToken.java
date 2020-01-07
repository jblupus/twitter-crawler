package com.jblupus.twittercrawler.model;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.io.Serializable;

/**
 * Created by joao on 11/30/16.
 */

@Table("oath_token")
public class OauthToken implements Serializable {
    @PrimaryKey
    private Long id;
    @Column("api_key")
    private String apiKey;
    @Column("api_secret")
    private String apiSecret;
    @Column("token")
    private String token;
    @Column("token_secret")
    private String tokenSecret;

    public OauthToken() {
    }

    public OauthToken(Long id) {
        this.id = id;
    }

    public OauthToken(Long id, String apiKey, String apiSecret, String token, String tokenSecret) {
        this.id = id;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OauthToken that = (OauthToken) o;

        if (!apiKey.equals(that.apiKey)) return false;
        if (!apiSecret.equals(that.apiSecret)) return false;
        if (!token.equals(that.token)) return false;
        return tokenSecret.equals(that.tokenSecret);

    }

    @Override
    public int hashCode() {
        int result = apiKey.hashCode();
        result = 31 * result + apiSecret.hashCode();
        result = 31 * result + token.hashCode();
        result = 31 * result + tokenSecret.hashCode();
        return result;
    }
}
