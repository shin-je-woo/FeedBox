package com.feedbox.infrastructure.mysql.outbox.repository;

import com.feedbox.infrastructure.mysql.outbox.entity.EventOutboxEntity;
import com.feedbox.infrastructure.mysql.outbox.model.EventOutboxType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EventOutboxRepository extends JpaRepository<EventOutboxEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from event_outbox e " +
            "where e.outboxType = :outboxType " +
            "and e.published = false " +
            "order by e.createdAt asc limit 1")
    Optional<EventOutboxEntity> findOldestEventByOutboxTypeWithLock(EventOutboxType outboxType);

    @Modifying
    @Query("update event_outbox " +
            "set published = true, " +
            "updatedAt = :updatedAt " +
            "where id = :outboxId")
    void updatePublishedByOutboxId(Long outboxId, LocalDateTime updatedAt);
}