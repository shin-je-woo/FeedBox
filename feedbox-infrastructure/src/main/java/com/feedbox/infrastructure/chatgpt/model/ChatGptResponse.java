package com.feedbox.infrastructure.chatgpt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatGptResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private String systemFingerprint;
    private Choice[] choices;
    private Usage usage;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private int index;
        private ChatGptMessage message;
        private Object logprobs;
        private String finishReason;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Usage {
        private int promptTokens;
        private int completionTokens;
        private int totalTokens;
    }
}
