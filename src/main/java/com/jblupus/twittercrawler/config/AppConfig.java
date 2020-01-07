package com.jblupus.twittercrawler.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by joao on 11/29/16.
 */
@Import({RepositoryConfig.class})
@Configuration
public class AppConfig {

    @Bean
    Gson gson(){
       return new Gson();
    }
}