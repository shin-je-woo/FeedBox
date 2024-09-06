package com.feedbox.infrastructure.chatgpt.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbox.infrastructure.chatgpt.model.*;
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

    public String instructAndAsk(ChatGptPolicy policy, String question) {
        List<ChatGptMessage> messages = createMessages(policy, question);
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

    private List<ChatGptMessage> createMessages(ChatGptPolicy policy, String question) {
        return List.of(
                // 지침을 설명한다.
                ChatGptMessage.of(ChatGptRole.SYSTEM.toString(), policy.getInstruction()),
                // 지침에 따른 예제 질문을 알려준다.
                ChatGptMessage.of(ChatGptRole.USER.toString(), policy.getExampleContent()),
                // 지침에 따른 예제 결과를 알려준다.
                ChatGptMessage.of(ChatGptRole.ASSISTANT.toString(), policy.getExampleContent()),
                // 실제 유저가 질문할 내용
                ChatGptMessage.of(ChatGptRole.USER.toString(), question));
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
