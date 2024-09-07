package com.feedbox.application.post.service;

import com.feedbox.application.post.port.in.PostResolvingUseCase;
import com.feedbox.application.post.port.out.PostPersistencePort;
import com.feedbox.application.post.port.out.ResolvedPostCachePort;
import com.feedbox.application.post.port.out.UserDataPort;
import com.feedbox.domain.model.Post;
import com.feedbox.domain.model.ResolvedPost;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostResolvingService implements PostResolvingUseCase {

    private final PostPersistencePort postPersistencePort;
    private final UserDataPort userDataPort;
    private final ResolvedPostCachePort resolvedPostCachePort;

    /**
     * Post + User 데이터 합쳐서 반환하는 로직
     * PostPersistencePort 통해 Post데이터 Get (Mysql)
     * UserDataPort를 통해 UserData Get (유저정보 서버 API)
     * 기존 데이터가 있으면 바로 반환 (Cache)
     *
     * Port를 통해 메시지를 주고 받기 때문에 Adapter의 세부 구현 내용을 몰라도 된다는 장점이 있음
     */
    @Override
    public ResolvedPost resolvePostById(Long postId) {
        ResolvedPost cacheData = resolvedPostCachePort.get(postId);
        if (cacheData != null) {
            log.info("[PostResolvingService] Cache에 데이터가 있어서 CacheData를 반환합니다. {}", cacheData);
            return cacheData;
        }
        Post post = postPersistencePort.findById(postId);
        String userName = userDataPort.getUserNameByUserId(post.getUserId());
        String categoryName = userDataPort.getCategoryNameByCategoryId(post.getCategoryId());
        ResolvedPost resolvedPost = ResolvedPost.of(post, userName, categoryName);
        log.info("[PostResolvingService] Cache에 데이터가 없어서 새로운 데이터를 반환합니다. {}", resolvedPost);
        resolvedPostCachePort.set(resolvedPost);
        return resolvedPost;
    }

    @Override
    public List<ResolvedPost> resolvePosts(List<Long> postIds) {
        // TODO 포스트 + 유저 데이터 합치기
        // 지금 로직이면 매번 컨텐츠마다 DB, API 호출하기 때문에 비효율
        return postIds.stream().map(this::resolvePostById).toList();
    }
}
