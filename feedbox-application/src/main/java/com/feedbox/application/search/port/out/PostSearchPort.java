package com.feedbox.application.search.port.out;

import com.feedbox.domain.model.post.InspectedPost;

import java.util.List;

public interface PostSearchPort {

    void indexPost(InspectedPost inspectedPost);
    void deletePost(Long postId);
    List<Long> searchPostIdsByKeyword(String keyword, int pageNumber, int pageSize);
}
