package com.feedbox.application.post.port.out;

import com.feedbox.domain.model.ResolvedPost;

public interface ResolvedPostCachePort {

    void set(ResolvedPost resolvedPost);
    ResolvedPost get(Long postId);
    void delete(Long postId);
}
