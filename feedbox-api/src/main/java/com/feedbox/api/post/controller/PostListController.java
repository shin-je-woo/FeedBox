package com.feedbox.api.post.controller;

import com.feedbox.api.post.model.dto.response.PostListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostListController {

    @GetMapping("/lists/{userId}") // 실제로는 HTTP 명세가 이렇지 않겠지만, 로그인 기능 생략으로
    public ResponseEntity<List<PostListResponse>> listSubscribingPosts(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/lists/search")
    public ResponseEntity<List<PostListResponse>> searchPosts(
            @RequestParam String query
    ) {
        return ResponseEntity.ok().build();
    }
}
