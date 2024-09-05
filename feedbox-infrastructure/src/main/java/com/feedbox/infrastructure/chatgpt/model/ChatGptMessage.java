package com.feedbox.infrastructure.chatgpt.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatGptMessage {
    private String role;
    private String content;

    public static ChatGptMessage of(String role, String content) {
        return new ChatGptMessage(role, content);
    }
}
