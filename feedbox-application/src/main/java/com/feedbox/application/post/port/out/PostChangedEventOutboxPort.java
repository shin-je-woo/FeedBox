package com.feedbox.application.post.port.out;

import com.feedbox.application.post.event.PostChangedEventPayload;
import com.feedbox.application.post.event.PostChangedEventOutbox;

public interface PostChangedEventOutboxPort {
    void save(PostChangedEventPayload postChangedEventPayload);
    PostChangedEventOutbox getOldestUnpublishedEvent();
    void completePublishedEvent(Long outboxId);
}
