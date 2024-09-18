package com.feedbox.application.post.event.handler;

import com.feedbox.application.post.event.PostChangedEventOutbox;
import com.feedbox.application.post.event.PostChangedEventPayload;
import com.feedbox.application.post.port.out.OriginalPostMessageProducePort;
import com.feedbox.application.post.port.out.PostChangedEventOutboxPort;
import com.feedbox.domain.model.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.feedbox.application.post.event.PostChangedEventPayload.OperationType.*;

/**
 * Transactional Outbox Pattern 에서 Message Relay 역할 담당 (Polling 방식으로 동작)
 */
@Component
@RequiredArgsConstructor
public class PostChangedEventPublishingJob {

    private final PostChangedEventOutboxPort outboxPort;
    private final OriginalPostMessageProducePort originalPostMessageProducePort;

    @Transactional
    @Scheduled(fixedDelay = 500)
    public void publishEvent() {
        PostChangedEventOutbox unpublishedEvent = outboxPort.getOldestUnpublishedEvent();
        if (unpublishedEvent == null) return;
        sendMessage(unpublishedEvent.getPostChangedEventPayload());
        outboxPort.completePublishedEvent(unpublishedEvent.getOutboxId());
    }

    private void sendMessage(PostChangedEventPayload postChangedEventPayload) {
        Post post = postChangedEventPayload.getPost();
        PostChangedEventPayload.OperationType operationType = postChangedEventPayload.getOperationType();
        if (CREATE.equals(operationType)) originalPostMessageProducePort.sendCreateMessage(post);
        if (UPDATE.equals(operationType)) originalPostMessageProducePort.sendUpdateMessage(post);
        if (DELETE.equals(operationType)) originalPostMessageProducePort.sendDeleteMessage(post.getId());
    }
}
