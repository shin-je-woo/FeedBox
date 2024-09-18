package com.feedbox.infrastructure.mysql.outbox.entity;

import com.feedbox.infrastructure.mysql.outbox.model.EventOutboxType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity(name = "event_outbox")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EventOutboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private EventOutboxType outboxType;
    private String eventPayload;
    private Boolean published;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static EventOutboxEntity of(@NonNull EventOutboxType outboxType, @NonNull String eventPayload) {
        return EventOutboxEntity.builder()
                .outboxType(outboxType)
                .eventPayload(eventPayload)
                .published(Boolean.FALSE)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
