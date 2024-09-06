package com.feedbox.infrastructure.mongodb.repository;

import com.feedbox.infrastructure.mongodb.document.SubscribingPostDocument;

import java.util.List;

public interface SubscribingPostCustomRepository {

    List<SubscribingPostDocument> findAllByFollowerIdWithPagination(Long followerId, int pageNumber, int pageSize);
    void deleteAllPostId(Long postId);
}
