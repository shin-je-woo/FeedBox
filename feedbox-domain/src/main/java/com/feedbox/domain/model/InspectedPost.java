package com.feedbox.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 검수된 Post
 */
@Getter
@Builder
public class InspectedPost {

    private Post post;
    private String categoryName;
    private List<String> tags;
    private LocalDateTime inspectedAt;

    public static InspectedPost of(
            Post post,
            String categoryName,
            List<String> tags,
            LocalDateTime inspectedAt
    ) {
        return InspectedPost.builder()
                .post(post)
                .categoryName(categoryName)
                .tags(tags)
                .inspectedAt(inspectedAt)
                .build();
    }
}
