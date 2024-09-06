package com.feedbox.infrastructure.mongodb.repository;

import com.feedbox.infrastructure.mongodb.document.SubscribingPostDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubscribingPostRepository extends SubscribingPostCustomRepository, MongoRepository<SubscribingPostDocument, String> {
}
