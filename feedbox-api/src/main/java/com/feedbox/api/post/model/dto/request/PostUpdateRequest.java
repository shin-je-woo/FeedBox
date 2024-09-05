package com.feedbox.api.post.model.dto.request;

import lombok.Getter;

@Getter
public class PostUpdateRequest {
    private String title;
    private String content;
    private Long categoryId;
}
