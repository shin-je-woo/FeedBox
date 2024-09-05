package com.feedbox.infrastructure.chatgpt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ChatGptWebClientConfig {

    @Bean
    public WebClient chatGptWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.openai.com")
                .build();
    }
}
