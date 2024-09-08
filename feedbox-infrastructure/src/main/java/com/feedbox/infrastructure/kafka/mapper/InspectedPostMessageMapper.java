package com.feedbox.infrastructure.kafka.mapper;

import com.feedbox.domain.model.post.InspectedPost;
import com.feedbox.infrastructure.kafka.common.OperationType;
import com.feedbox.infrastructure.kafka.message.InspectedPostMessage;

public class InspectedPostMessageMapper {

    public static InspectedPostMessage toMessage(InspectedPost inspectedPost, OperationType operationType) {
        return InspectedPostMessage.builder()
                .id(inspectedPost.getPost().getId())
                .operationType(operationType)
                .payload(InspectedPostMessage.Payload.builder()
                        .post(inspectedPost.getPost())
                        .categoryName(inspectedPost.getCategoryName())
                        .tags(inspectedPost.getTags())
                        .inspectedAt(inspectedPost.getInspectedAt())
                        .build()
                )
                .build();
    }

    public static InspectedPost toDomain(InspectedPostMessage inspectedPostMessage) {
        return InspectedPost.builder()
                .post(inspectedPostMessage.getPayload().getPost())
                .categoryName(inspectedPostMessage.getPayload().getCategoryName())
                .tags(inspectedPostMessage.getPayload().getTags())
                .inspectedAt(inspectedPostMessage.getPayload().getInspectedAt())
                .build();
    }
}
