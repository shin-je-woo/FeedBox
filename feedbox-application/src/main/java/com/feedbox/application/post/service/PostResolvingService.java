package com.feedbox.application.post.service;

import com.feedbox.application.post.port.in.PostResolvingUseCase;
import com.feedbox.application.post.port.out.UserDataPort;
import com.feedbox.domain.model.ResolvedPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostResolvingService implements PostResolvingUseCase {

    private final UserDataPort userDataPort;

    @Override
    public ResolvedPost resolvePostById(Long postId) {
        // TODO 포스트 + 유저 데이터 합치기
        return null;
    }

    @Override
    public List<ResolvedPost> resolvePosts(List<Long> postIds) {
        // TODO 포스트 + 유저 데이터 합치기
        return List.of();
    }
}
