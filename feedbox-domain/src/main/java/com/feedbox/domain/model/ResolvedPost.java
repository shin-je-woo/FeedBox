package com.feedbox.domain.model;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Post + 기타 정보
 */
@Getter
@Builder
public class ResolvedPost {

    private Long id;
    private String title;
    private String content;
    private Long userId;
    private String userName;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean updated;

    public static ResolvedPost of(Post post, String userName, String categoryName) {
        return ResolvedPost.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .userId(post.getUserId())
                .userName(userName)
                .categoryId(post.getCategoryId())
                .categoryName(categoryName)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .updated(!post.getCreatedAt().equals(post.getUpdatedAt()))
                .build();
    }
}