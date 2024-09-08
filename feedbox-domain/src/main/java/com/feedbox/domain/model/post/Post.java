package com.feedbox.domain.model.post;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Post {

    private Long id;
    private String title;
    private String content;
    private Long userId;
    private Long categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static Post of(String title, String content, Long userId, Long categoryId) {
        LocalDateTime now = LocalDateTime.now();
        return Post.builder()
                .title(title)
                .content(content)
                .userId(userId)
                .categoryId(categoryId)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public Post update(String title, String content, Long categoryId) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public Post delete() {
        LocalDateTime now = LocalDateTime.now();
        this.updatedAt = now;
        this.deletedAt = now;
        return this;
    }

    public Post undelete() {
        this.deletedAt = null;
        return this;
    }
}