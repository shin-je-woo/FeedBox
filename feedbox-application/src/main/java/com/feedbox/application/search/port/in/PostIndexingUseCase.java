package com.feedbox.application.search.port.in;

import com.feedbox.domain.model.InspectedPost;

public interface PostIndexingUseCase {

    void save(InspectedPost inspectedPost);
    void delete(Long postId);
}
