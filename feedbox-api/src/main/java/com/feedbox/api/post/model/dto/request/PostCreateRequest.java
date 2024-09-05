package com.feedbox.api.post.model.dto.request;

import lombok.Getter;

@Getter
public class PostCreateRequest {
    private String title;
    private String content;
    private Long userId;
    private Long categoryId;
}
