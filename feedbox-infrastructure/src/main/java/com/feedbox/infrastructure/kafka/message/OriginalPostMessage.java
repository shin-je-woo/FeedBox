package com.feedbox.infrastructure.kafka.message;

import com.feedbox.infrastructure.kafka.common.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OriginalPostMessage {

    private Long id;
    private Payload payload;
    private OperationType operationType;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload {
        private Long id;
        private String title;
        private String content;
        private Long userId;
        private Long categoryId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime deletedAt;
    }
}
