package com.feedbox.infrastructure.chatgpt.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbox.infrastructure.chatgpt.model.ChatGptMessage;
import com.feedbox.infrastructure.chatgpt.model.ChatGptRequest;
import com.feedbox.infrastructure.chatgpt.model.ChatGptResponse;
import com.feedbox.infrastructure.chatgpt.model.ChatGptRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatGptClient {

    private final WebClient chatGptWebClient;
    private final ObjectMapper objectMapper;
    @Value("${CHAT_GPT_API_KEY}")
    private String apiKey;
    private static final String REQUEST_URI = "/v1/chat/completions";
    private static final String TOKEN_TYPE = "Bearer ";
    private static final String GPT_MODEL = "gpt-3.5-turbo";

    public String testChatGpt(String content) {
        List<ChatGptMessage> messages = createMessages(content);
        String result = chatGptWebClient.post()
                .uri(REQUEST_URI)
                .header(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(
                        ChatGptRequest.builder()
                                .model(GPT_MODEL)
                                .messages(messages)
                                .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return extractContent(result);
    }

    private List<ChatGptMessage> createMessages(String content) {
        return List.of(
                ChatGptMessage.of(ChatGptRole.SYSTEM.toString(), "너는 나의 박사님이야"),
                ChatGptMessage.of(ChatGptRole.USER.toString(), content));
    }

    private String extractContent(String result) {
        try {
            ChatGptResponse chatGptResponse = objectMapper.readValue(result, ChatGptResponse.class);
            return chatGptResponse.getChoices()[0].getMessage().getContent();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
