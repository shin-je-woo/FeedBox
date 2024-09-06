package com.feedbox.application.subscribing.port.out;

import com.feedbox.domain.model.Post;

import java.util.List;

public interface SubscribingPostPort {

    /**
     * 컨텐츠를 구독자의 구독함에 추가한다.
     */
    void addPostToFollowerInboxes(Post post, List<Long> followerIds);

    /**
     * 구독하고 있는 User가 생산한 컨텐츠 목록을 조회한다.
     */
    List<Long> listPostIdsByFollowerIdWithPagination(Long followerId, int pageNumber, int pageSize);
}
