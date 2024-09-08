package com.feedbox.api.post.controller;

import com.feedbox.api.post.model.dto.response.PostListResponse;
import com.feedbox.api.post.model.mapper.ResolvedPostMapper;
import com.feedbox.application.search.port.in.PostSearchUseCase;
import com.feedbox.application.subscribing.port.in.SubscribingPostListUseCase;
import com.feedbox.domain.model.ResolvedPost;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/lists")
public class PostListController {

    private final SubscribingPostListUseCase subscribingPostListUseCase;
    private final PostSearchUseCase postSearchUseCase;

    @GetMapping("/inbox/{userId}") // 실제로는 HTTP 명세가 이렇지 않겠지만, 로그인 기능 생략으로
    public ResponseEntity<List<PostListResponse>> listSubscribingPosts(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "0") int page
    ) {
        List<ResolvedPost> resolvedPosts = subscribingPostListUseCase.listSubscribingInboxPosts(userId, page);
        List<PostListResponse> postListResponses = resolvedPosts.stream()
                .map(ResolvedPostMapper::toListResponse)
                .toList();
        return ResponseEntity.ok().body(postListResponses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostListResponse>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "0") int page
    ) {
        List<ResolvedPost> searchPosts = postSearchUseCase.getSearchResultByKeyword(keyword, page);
        List<PostListResponse> postListResponses = searchPosts.stream()
                .map(ResolvedPostMapper::toListResponse)
                .toList();
        return ResponseEntity.ok().body(postListResponses);
    }
}
