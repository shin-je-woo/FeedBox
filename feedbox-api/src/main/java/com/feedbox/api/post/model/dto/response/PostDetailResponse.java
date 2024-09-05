package com.feedbox.api.post.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostDetailResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String userName;
    private final String categoryName;
    private final LocalDateTime createdAt;
    private final boolean updated;
}
