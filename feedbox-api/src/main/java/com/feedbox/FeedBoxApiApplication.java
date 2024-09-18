package com.feedbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableRetry
@EnableAsync
@SpringBootApplication
public class FeedBoxApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedBoxApiApplication.class, args);
    }
}
