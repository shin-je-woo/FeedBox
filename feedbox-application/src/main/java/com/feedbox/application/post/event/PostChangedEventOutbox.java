package com.feedbox.application.post.event;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostChangedEventOutbox {

    private final Long outboxId;
    private final PostChangedEventPayload postChangedEventPayload;

    public static PostChangedEventOutbox of(Long outboxId, PostChangedEventPayload postChangedEventPayload) {
        return PostChangedEventOutbox.builder()
                .outboxId(outboxId)
                .postChangedEventPayload(postChangedEventPayload)
                .build();
    }
}
