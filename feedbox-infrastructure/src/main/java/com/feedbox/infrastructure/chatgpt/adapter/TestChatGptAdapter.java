package com.feedbox.infrastructure.chatgpt.adapter;

import com.feedbox.application.post.port.out.TestChatGptPort;
import com.feedbox.infrastructure.chatgpt.client.ChatGptClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestChatGptAdapter implements TestChatGptPort {

    private final ChatGptClient chatGptClient;

    @Override
    public String test(String content) {
        return chatGptClient.testChatGpt(content);
    }
}
