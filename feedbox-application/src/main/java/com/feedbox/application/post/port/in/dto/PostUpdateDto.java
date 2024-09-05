package com.feedbox.application.post.port.in.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostUpdateDto {
    private final Long postId;
    private final String title;
    private final String content;
    private final Long categoryId;
}
