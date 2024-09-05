package com.feedbox.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.feedbox"})
@SpringBootApplication
public class FeedBoxApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedBoxApiApplication.class, args);
    }
}
