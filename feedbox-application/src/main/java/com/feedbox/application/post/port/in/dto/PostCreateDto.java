package com.feedbox.application.post.port.in.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCreateDto {
    private final Long userId;
    private final String title;
    private final String content;
    private final Long categoryId;
}
