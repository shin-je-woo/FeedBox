package com.feedbox.infrastructure.chatgpt.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatGptPolicy {
    private final String instruction;
    private final String exampleContent;
    private final String exampleResult;
}
