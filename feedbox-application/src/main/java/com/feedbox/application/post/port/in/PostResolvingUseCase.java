package com.feedbox.application.post.port.in;

import com.feedbox.domain.model.post.Post;
import com.feedbox.domain.model.post.ResolvedPost;

import java.util.List;

public interface PostResolvingUseCase {

    ResolvedPost resolvePostById(Long postId);
    List<ResolvedPost> resolvePosts(List<Long> postIds);
    void resolveAndSaveCache(Post post);
    void deleteCachedResolvedPost(Long postId);
}
