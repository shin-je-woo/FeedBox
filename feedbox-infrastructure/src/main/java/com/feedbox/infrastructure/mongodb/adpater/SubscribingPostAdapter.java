package com.feedbox.infrastructure.mongodb.adpater;

import com.feedbox.application.subscribing.port.out.SubscribingPostPort;
import com.feedbox.domain.model.post.Post;
import com.feedbox.infrastructure.mongodb.document.SubscribingPostDocument;
import com.feedbox.infrastructure.mongodb.repository.SubscribingPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscribingPostAdapter implements SubscribingPostPort {

    private final SubscribingPostRepository subscribingPostRepository;

    @Override
    public void addPostToFollowerInboxes(Post post, List<Long> followerIds) {
        List<SubscribingPostDocument> subscribingPosts = followerIds.stream()
                .map(followerId -> SubscribingPostDocument.of(post, followerId))
                .toList();
        subscribingPostRepository.saveAll(subscribingPosts);
    }

    @Override
    public void removePostFromFollowerInboxes(Long postId) {
        subscribingPostRepository.deleteAllPostId(postId);
    }

    @Override
    public List<Long> listPostIdsByFollowerIdWithPagination(Long followerId, int pageNumber, int pageSize) {
        return subscribingPostRepository.findAllByFollowerIdWithPagination(followerId, pageNumber, pageSize)
                .stream()
                .map(SubscribingPostDocument::getPostId)
                .toList();
    }
}
