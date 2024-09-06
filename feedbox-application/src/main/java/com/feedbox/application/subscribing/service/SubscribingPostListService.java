package com.feedbox.application.subscribing.service;

import com.feedbox.application.post.port.in.PostResolvingUseCase;
import com.feedbox.application.subscribing.port.in.SubscribingPostListUseCase;
import com.feedbox.application.subscribing.port.out.SubscribingPostPort;
import com.feedbox.domain.model.ResolvedPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribingPostListService implements SubscribingPostListUseCase {

    private static final int PAGE_SIZE = 5;
    private final SubscribingPostPort subscribingPostPort;
    private final PostResolvingUseCase postResolvingUseCase;

    @Override
    public List<ResolvedPost> listSubscribingInboxPosts(Long userId, int pageNumber) {
        List<Long> subscribingPostIds = subscribingPostPort.listPostIdsByFollowerIdWithPagination(userId, pageNumber, PAGE_SIZE);
        return postResolvingUseCase.resolvePosts(subscribingPostIds);
    }
}