package com.feedbox.application.post.event;

import com.feedbox.domain.model.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@Builder
@ToString
public class PostChangedEventPayload {

    private final Post post;
    private final OperationType operationType;

    public static PostChangedEventPayload create(@NonNull Post post, @NonNull OperationType operationType) {
        return PostChangedEventPayload.builder()
                .post(post)
                .operationType(operationType)
                .build();
    }

    public enum OperationType {
        CREATE,
        UPDATE,
        DELETE
    }
}
