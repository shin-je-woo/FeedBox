package com.feedbox.application.subscribing.service;

import com.feedbox.application.post.port.out.UserDataPort;
import com.feedbox.application.subscribing.port.in.SubscribingPostAddToInboxUseCase;
import com.feedbox.application.subscribing.port.in.SubscribingPostRemoveFromInboxUseCase;
import com.feedbox.application.subscribing.port.out.SubscribingPostPort;
import com.feedbox.domain.model.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribingPostService implements SubscribingPostAddToInboxUseCase, SubscribingPostRemoveFromInboxUseCase {

    private final UserDataPort userDataPort;
    private final SubscribingPostPort subscribingPostPort;

    /**
     * 유저정보 API 호출해서 구독자 정보를 가져온 뒤 MongoDB에 저장한다.
     */
    @Override
    public void saveSubscribingInboxPost(Post post) {
        List<Long> followerIds = userDataPort.getFollowerIdsByUserId(post.getUserId());
        subscribingPostPort.addPostToFollowerInboxes(post, followerIds);
    }

    @Override
    public void removeSubscribingPostFromInbox(Long postId) {
        subscribingPostPort.removePostFromFollowerInboxes(postId);
    }
}
