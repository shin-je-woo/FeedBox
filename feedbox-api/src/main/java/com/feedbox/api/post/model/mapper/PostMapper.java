package com.feedbox.api.post.model.mapper;

import com.feedbox.api.post.model.dto.request.PostCreateRequest;
import com.feedbox.api.post.model.dto.request.PostUpdateRequest;
import com.feedbox.api.post.model.dto.response.PostDetailResponse;
import com.feedbox.api.post.model.dto.response.PostResponse;
import com.feedbox.application.post.port.in.dto.PostCreateDto;
import com.feedbox.application.post.port.in.dto.PostUpdateDto;
import com.feedbox.domain.model.post.Post;
import com.feedbox.domain.model.post.ResolvedPost;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostMapper {

    public static PostCreateDto toDto(PostCreateRequest request) {
        return PostCreateDto.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .userId(request.getUserId())
                .categoryId(request.getCategoryId())
                .build();
    }

    public static PostUpdateDto toDto(PostUpdateRequest request, Long postId) {
        return PostUpdateDto.builder()
                .postId(postId)
                .title(request.getTitle())
                .content(request.getContent())
                .categoryId(request.getCategoryId())
                .build();
    }

    public static PostResponse toResponse(Post post) {
        return PostResponse.builder()
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

    public static PostDetailResponse toDetailResponse(ResolvedPost resolvedPost) {
        return PostDetailResponse.builder()
                .id(resolvedPost.getId())
                .title(resolvedPost.getTitle())
                .content(resolvedPost.getContent())
                .userName(resolvedPost.getUserName())
                .categoryName(resolvedPost.getCategoryName())
                .createdAt(resolvedPost.getCreatedAt())
                .updated(resolvedPost.isUpdated())
                .build();
    }
}
