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
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
     * <p>
     * Port를 통해 메시지를 주고 받기 때문에 Adapter의 세부 구현 내용을 몰라도 된다는 장점이 있음
     */
    @Override
    public ResolvedPost resolvePostById(Long postId) {
        ResolvedPost cacheData = resolvedPostCachePort.get(postId);
        if (cacheData != null) {
            log.info("[PostResolvingService] Cache에 resolvedPost가 있어서 CacheData를 반환합니다. {}", cacheData);
            return cacheData;
        }
        Post post = postPersistencePort.findById(postId);
        return resolvePost(post);
    }

    @Override
    public List<ResolvedPost> resolvePosts(List<Long> postIds) {
        if (CollectionUtils.isEmpty(postIds)) return Collections.emptyList();
        ArrayList<ResolvedPost> resolvedPosts = new ArrayList<>();
        // 1. 캐시 저장소(Redis)에서 multiGet으로 컨텐츠를 가져온다.
        List<ResolvedPost> cacheHitPosts = resolvedPostCachePort.getList(postIds);
        resolvedPosts.addAll(cacheHitPosts);
        // 2. Cache Miss된 컨텐츠를 찾아서 조회한다.
        List<Long> cacheMissPostIds = getNotMatchedPostIds(postIds, cacheHitPosts);
        List<Post> missingPosts = postPersistencePort.listByIds(cacheMissPostIds);
        List<ResolvedPost> missingResolvedPosts = missingPosts.stream()
                .filter(Objects::nonNull)
                .map(this::resolvePost)
                .toList();
        resolvedPosts.addAll(missingResolvedPosts);
        // 3. 요청한 postIds 순서대로 정렬해서 반환한다.
        Map<Long, ResolvedPost> resolvedPostMap = resolvedPosts.stream()
                .collect(Collectors.toMap(ResolvedPost::getId, Function.identity()));
        return postIds.stream()
                .map(resolvedPostMap::get)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public void resolveAndSaveCache(Post post) {
        ResolvedPost resolvedPost = resolvePost(post);
        resolvedPostCachePort.set(resolvedPost);
        log.info("[PostResolvingService] Cache에 resolvedPost를 저장했습니다. {}", resolvedPost);
    }

    @Override
    public void deleteCachedResolvedPost(Long postId) {
        resolvedPostCachePort.delete(postId);
    }

    private ResolvedPost resolvePost(Post post) {
        String userName = userDataPort.getUserNameByUserId(post.getUserId());
        String categoryName = userDataPort.getCategoryNameByCategoryId(post.getCategoryId());
        ResolvedPost resolvedPost = ResolvedPost.of(post, userName, categoryName);
        resolvedPostCachePort.set(resolvedPost);
        log.info("[PostResolvingService] Cache에 resolvedPost를 저장했습니다. {}", resolvedPost);
        return resolvedPost;
    }

    private List<Long> getNotMatchedPostIds(List<Long> postIds, List<ResolvedPost> cacheHitPosts) {
        return postIds.stream()
                .filter(postId -> cacheHitPosts.stream()
                        .noneMatch(resolvedPost -> resolvedPost.getId().equals(postId))
                ).toList();
    }
}
