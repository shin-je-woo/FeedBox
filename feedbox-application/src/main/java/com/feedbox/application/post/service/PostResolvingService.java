package com.feedbox.application.post.service;

import com.feedbox.application.post.port.in.PostResolvingUseCase;
import com.feedbox.application.post.port.out.PostPersistencePort;
import com.feedbox.application.post.port.out.UserDataPort;
import com.feedbox.domain.model.Post;
import com.feedbox.domain.model.ResolvedPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostResolvingService implements PostResolvingUseCase {

    private final PostPersistencePort postPersistencePort;
    private final UserDataPort userDataPort;

    /**
     * Post + User 데이터 합쳐서 반환
     * PostPort를 통해 Post데이터 Get
     * UserDataPort를 통해 UserData Get
     *
     * Port를 통해 메시지를 주고 받기 때문에 Adapter의 세부 구현 내용을 몰라도 된다는 장점이 있음
     */
    @Override
    public ResolvedPost resolvePostById(Long postId) {
        Post post = postPersistencePort.findById(postId);
        String userName = userDataPort.getUserNameByUserId(post.getUserId());
        String categoryName = userDataPort.getCategoryNameByCategoryId(post.getCategoryId());
        return ResolvedPost.of(post, userName, categoryName);
    }

    @Override
    public List<ResolvedPost> resolvePosts(List<Long> postIds) {
        // TODO 포스트 + 유저 데이터 합치기
        // 지금 로직이면 매번 컨텐츠마다 DB, API 호출하기 때문에 비효율
        return postIds.stream().map(this::resolvePostById).toList();
    }
}
