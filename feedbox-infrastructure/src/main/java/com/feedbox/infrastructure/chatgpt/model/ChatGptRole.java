package com.feedbox.infrastructure.chatgpt.model;

public enum ChatGptRole {
    SYSTEM,
    USER;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
