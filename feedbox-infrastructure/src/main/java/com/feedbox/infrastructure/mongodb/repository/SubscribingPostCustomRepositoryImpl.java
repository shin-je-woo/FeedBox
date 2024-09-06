package com.feedbox.infrastructure.mongodb.repository;

import com.feedbox.infrastructure.mongodb.document.SubscribingPostDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SubscribingPostCustomRepositoryImpl implements SubscribingPostCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<SubscribingPostDocument> findAllByFollowerIdWithPagination(
            Long followerId,
            int pageNumber,
            int pageSize
    ) {
        Query query = new Query()
                .addCriteria(Criteria.where("followerId").is(followerId))
                .with(PageRequest.of(
                        pageNumber,
                        pageSize,
                        Sort.by(Sort.Direction.DESC, "postCreatedAt")
                        )
                );
        log.info("[SubscribingPostCustomRepositoryImpl] query = {}", query);
        return mongoTemplate.find(query, SubscribingPostDocument.class, "subscribingInboxPosts");
    }

    @Override
    public void deleteAllPostId(Long postId) {
        Query query = new Query()
                .addCriteria(Criteria.where("postId").is(postId));
        mongoTemplate.remove(query, SubscribingPostDocument.class);
    }
}
