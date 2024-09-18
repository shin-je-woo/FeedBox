package com.feedbox.infrastructure.mysql.post.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbox.application.post.event.PostChangedEventOutbox;
import com.feedbox.application.post.event.PostChangedEventPayload;
import com.feedbox.application.post.port.out.PostChangedEventOutboxPort;
import com.feedbox.infrastructure.mysql.outbox.entity.EventOutboxEntity;
import com.feedbox.infrastructure.mysql.outbox.model.EventOutboxType;
import com.feedbox.infrastructure.mysql.outbox.repository.EventOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
public class PostChangedEventOutboxAdapter implements PostChangedEventOutboxPort {

    private final ObjectMapper objectMapper;
    private final EventOutboxRepository eventOutboxRepository;

    @Override
    public void save(PostChangedEventPayload postChangedEventPayload) {
        String postChangedEventJson = convertJson(postChangedEventPayload);
        EventOutboxEntity eventOutboxEntity = EventOutboxEntity.of(
                EventOutboxType.POST,
                postChangedEventJson
        );
        eventOutboxRepository.save(eventOutboxEntity);
    }

    @Override
    public PostChangedEventOutbox getOldestUnpublishedEvent() {
        AtomicReference<PostChangedEventOutbox> result = new AtomicReference<>();
        eventOutboxRepository.findOldestEventByOutboxTypeWithLock(EventOutboxType.POST)
                .ifPresentOrElse(
                        outbox -> result.set(PostChangedEventOutbox.of(outbox.getId(), convertObject(outbox.getEventPayload()))),
                        () -> result.set(null));
        return result.get();
    }

    @Override
    public void completePublishedEvent(Long outboxId) {
        eventOutboxRepository.updatePublishedByOutboxId(outboxId, LocalDateTime.now());
    }

    private String convertJson(PostChangedEventPayload postChangedEventPayload) {
        try {
            return objectMapper.writeValueAsString(postChangedEventPayload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private PostChangedEventPayload convertObject(String postChangedEventJson) {
        try {
            return objectMapper.readValue(postChangedEventJson, PostChangedEventPayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
