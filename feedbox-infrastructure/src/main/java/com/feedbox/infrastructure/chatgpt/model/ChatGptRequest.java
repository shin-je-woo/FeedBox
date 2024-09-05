package com.feedbox.infrastructure.chatgpt.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatGptRequest {
    private String model;
    private List<ChatGptMessage> messages;
}
