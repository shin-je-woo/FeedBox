package com.feedbox.api.post.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostListResponse {
    private final Long id;
    private final String title;
    private final String userName;
    private final LocalDateTime createdAt;
}
