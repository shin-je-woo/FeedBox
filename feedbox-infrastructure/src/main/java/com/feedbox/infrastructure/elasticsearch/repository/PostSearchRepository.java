package com.feedbox.infrastructure.elasticsearch.repository;

import com.feedbox.infrastructure.elasticsearch.document.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, Long> {
}
