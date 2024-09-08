package com.feedbox.application.search.port.in;

import com.feedbox.domain.model.post.InspectedPost;

public interface PostIndexingUseCase {

    void save(InspectedPost inspectedPost);
    void delete(Long postId);
}
