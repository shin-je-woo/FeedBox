package com.feedbox.infrastructure.kafka.mapper;

import com.feedbox.domain.model.Post;
import com.feedbox.infrastructure.kafka.common.OperationType;
import com.feedbox.infrastructure.kafka.message.OriginalPostMessage;

public class OriginalPostMessageMapper {

    public static OriginalPostMessage toMessage(Long id, Post post, OperationType operationType) {
        return OriginalPostMessage.builder()
                .id(id)
                .operationType(operationType)
                .payload(post == null ? null :
                        OriginalPostMessage.Payload.builder()
                                .id(post.getId())
                                .title(post.getTitle())
                                .content(post.getContent())
                                .userId(post.getUserId())
                                .categoryId(post.getCategoryId())
                                .createdAt(post.getCreatedAt())
                                .updatedAt(post.getUpdatedAt())
                                .deletedAt(post.getDeletedAt())
                                .build()
                )
                .build();
    }

    public static Post toDomain(OriginalPostMessage originalPostMessage) {
        return Post.builder()
                .id(originalPostMessage.getPayload().getId())
                .title(originalPostMessage.getPayload().getTitle())
                .content(originalPostMessage.getPayload().getContent())
                .userId(originalPostMessage.getPayload().getUserId())
                .categoryId(originalPostMessage.getPayload().getCategoryId())
                .createdAt(originalPostMessage.getPayload().getCreatedAt())
                .updatedAt(originalPostMessage.getPayload().getUpdatedAt())
                .deletedAt(originalPostMessage.getPayload().getDeletedAt())
                .build();
    }
}
