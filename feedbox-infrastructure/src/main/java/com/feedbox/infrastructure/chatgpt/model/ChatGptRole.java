package com.feedbox.infrastructure.chatgpt.model;

public enum ChatGptRole {
    SYSTEM,
    ASSISTANT,
    USER;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
