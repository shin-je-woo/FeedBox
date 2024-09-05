package com.feedbox.application.post.port.in;

import com.feedbox.domain.model.ResolvedPost;

import java.util.List;

public interface PostResolvingUseCase {

    ResolvedPost resolvePostById(Long postId);
    List<ResolvedPost> resolvePosts(List<Long> postIds);
}
