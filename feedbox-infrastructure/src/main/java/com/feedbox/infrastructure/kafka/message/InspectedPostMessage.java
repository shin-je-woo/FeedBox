package com.feedbox.infrastructure.kafka.message;

import com.feedbox.domain.model.post.Post;
import com.feedbox.infrastructure.kafka.common.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectedPostMessage {

    private Long id;
    private Payload payload;
    private OperationType operationType;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload {
        private Post post;
        private String categoryName;
        private List<String> tags;
        private LocalDateTime inspectedAt;
    }
}
