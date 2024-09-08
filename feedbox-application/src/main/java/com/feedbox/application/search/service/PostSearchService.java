package com.feedbox.application.search.service;

import com.feedbox.application.search.port.in.PostIndexingUseCase;
import com.feedbox.application.search.port.out.PostSearchPort;
import com.feedbox.domain.model.InspectedPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostSearchService implements PostIndexingUseCase {

    private final PostSearchPort postSearchPort;

    @Override
    public void save(InspectedPost inspectedPost) {
        postSearchPort.indexPost(inspectedPost);
    }

    @Override
    public void delete(Long postId) {
        postSearchPort.deletePost(postId);
    }
}
