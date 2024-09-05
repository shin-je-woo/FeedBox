package com.feedbox.infrastructure.mysql.post.mapper;

import com.feedbox.domain.model.Post;
import com.feedbox.infrastructure.mysql.post.entity.PostEntity;

public class PostEntityMapper {

    public static PostEntity toEntity(Post post) {
        return PostEntity.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .userId(post.getUserId())
                .categoryId(post.getCategoryId())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .deletedAt(post.getDeletedAt())
                .build();
    }

    public static Post toDomain(PostEntity postEntity) {
        return Post.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .userId(postEntity.getUserId())
                .categoryId(postEntity.getCategoryId())
                .createdAt(postEntity.getCreatedAt())
                .updatedAt(postEntity.getUpdatedAt())
                .deletedAt(postEntity.getDeletedAt())
                .build();
    }
}
