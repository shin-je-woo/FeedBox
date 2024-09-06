package com.feedbox.application.subscribing.port.in;

import com.feedbox.domain.model.ResolvedPost;

import java.util.List;

public interface SubscribingPostListUseCase {

    /**
     * 구독함에 있는 컨텐츠 목록을 조회한다.
     */
    List<ResolvedPost> listSubscribingInboxPosts(Long userId, int pageNumber);
}
