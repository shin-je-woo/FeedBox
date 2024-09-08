package com.feedbox.infrastructure.elasticsearch.mapper;

import com.feedbox.domain.model.post.InspectedPost;
import com.feedbox.infrastructure.elasticsearch.document.PostDocument;

import java.time.LocalDateTime;

public class PostDocumentMapper {

    public static PostDocument toDocument(InspectedPost inspectedPost) {
        return PostDocument.builder()
                .id(inspectedPost.getPost().getId())
                .title(inspectedPost.getPost().getTitle())
                .content(inspectedPost.getPost().getContent())
                .categoryName(inspectedPost.getCategoryName())
                .tags(inspectedPost.getTags())
                .indexedAt(LocalDateTime.now())
                .build();
    }
}
