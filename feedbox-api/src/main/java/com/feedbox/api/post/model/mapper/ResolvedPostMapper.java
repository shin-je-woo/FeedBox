package com.feedbox.api.post.model.mapper;

import com.feedbox.api.post.model.dto.response.PostListResponse;
import com.feedbox.domain.model.post.ResolvedPost;

public class ResolvedPostMapper {

    public static PostListResponse toListResponse(final ResolvedPost resolvedPost) {
        return PostListResponse.builder()
                .id(resolvedPost.getId())
                .title(resolvedPost.getTitle())
                .userName(resolvedPost.getUserName())
                .createdAt(resolvedPost.getCreatedAt())
                .build();
    }
}
