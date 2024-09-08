package com.feedbox.application.search.port.out;

import com.feedbox.domain.model.InspectedPost;

public interface PostSearchPort {

    void indexPost(InspectedPost inspectedPost);
    void deletePost(Long postId);
}
