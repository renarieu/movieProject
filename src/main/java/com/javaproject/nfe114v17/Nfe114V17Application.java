package com.javaproject.nfe114v17;

import com.javaproject.nfe114v17.tmdbApi.TmdbApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication


public class Nfe114V17Application {

    @Value("${tmdb.apikey}")
    private String tmdbApiKey ;

    @Bean
    public TmdbApiClient getTmdbApiClient(){
        return new TmdbApiClient(tmdbApiKey);
    }

    public static void main(String[] args) {
        SpringApplication.run(Nfe114V17Application.class, args);
    }

}
