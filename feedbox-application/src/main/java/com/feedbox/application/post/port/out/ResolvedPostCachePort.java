package com.feedbox.application.post.port.out;

import com.feedbox.domain.model.post.ResolvedPost;

import java.util.List;

public interface ResolvedPostCachePort {

    void set(ResolvedPost resolvedPost);
    ResolvedPost get(Long postId);
    List<ResolvedPost> getList(List<Long> postIds);
    void delete(Long postId);
}
