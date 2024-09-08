package com.feedbox.infrastructure.elasticsearch.adapter;

import com.feedbox.application.search.port.out.PostSearchPort;
import com.feedbox.domain.model.InspectedPost;
import com.feedbox.infrastructure.elasticsearch.document.PostDocument;
import com.feedbox.infrastructure.elasticsearch.mapper.PostDocumentMapper;
import com.feedbox.infrastructure.elasticsearch.repository.PostSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostSearchAdapter implements PostSearchPort {

    private final PostSearchRepository postSearchRepository;

    @Override
    public void indexPost(InspectedPost inspectedPost) {
        PostDocument postDocument = PostDocumentMapper.toDocument(inspectedPost);
        postSearchRepository.save(postDocument);
    }

    @Override
    public void deletePost(Long postId) {
        postSearchRepository.deleteById(postId);
    }
}
