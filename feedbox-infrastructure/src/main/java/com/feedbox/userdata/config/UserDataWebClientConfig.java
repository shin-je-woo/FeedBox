package com.feedbox.userdata.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class UserDataWebClientConfig {

    @Value("${userdata-server.url}")
    private String userApiUrl;

    @Bean
    public WebClient userDataWebClient() {
        return WebClient.builder()
                .baseUrl(userApiUrl)
                .build();
    }
}
