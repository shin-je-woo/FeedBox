package com.feedbox.infrastructure.elasticsearch.adapter;

import com.feedbox.application.search.port.out.PostSearchPort;
import com.feedbox.domain.model.post.InspectedPost;
import com.feedbox.infrastructure.elasticsearch.document.PostDocument;
import com.feedbox.infrastructure.elasticsearch.mapper.PostDocumentMapper;
import com.feedbox.infrastructure.elasticsearch.repository.PostSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PostSearchAdapter implements PostSearchPort {

    private final ElasticsearchOperations elasticsearchOperations;
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

    @Override
    public List<Long> searchPostIdsByKeyword(String keyword, int pageNumber, int pageSize) {
        if (!StringUtils.hasText(keyword) || pageNumber < 0 || pageSize < 0) {
            return Collections.emptyList();
        }

        Query query = buildQuery(keyword, pageNumber, pageSize);
        SearchHits<PostDocument> searchHits = elasticsearchOperations.search(query, PostDocument.class);

        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(PostDocument::getId)
                .toList();
    }

    private Query buildQuery(String keyword, int pageNumber, int pageSize) {
        Criteria criteria = new Criteria("title").matches(keyword)
                .or(new Criteria("content").matches(keyword))
                .or(new Criteria("categoryName").is(keyword))
                .or(new Criteria("tags").is(keyword));

        return new CriteriaQuery(criteria)
                .setPageable(PageRequest.of(pageNumber, pageSize));
    }
}
