package com.feedbox.api.post.controller;

import com.feedbox.api.post.model.dto.request.PostCreateRequest;
import com.feedbox.api.post.model.dto.request.PostUpdateRequest;
import com.feedbox.api.post.model.dto.response.PostDetailResponse;
import com.feedbox.api.post.model.dto.response.PostResponse;
import com.feedbox.api.post.model.mapper.PostMapper;
import com.feedbox.application.post.port.in.PostCreateUseCase;
import com.feedbox.application.post.port.in.PostDeleteUseCase;
import com.feedbox.application.post.port.in.PostReadUseCase;
import com.feedbox.application.post.port.in.PostUpdateUseCase;
import com.feedbox.domain.model.post.Post;
import com.feedbox.domain.model.post.ResolvedPost;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostCreateUseCase postCreateUseCase;
    private final PostReadUseCase postReadUseCase;
    private final PostUpdateUseCase postUpdateUseCase;
    private final PostDeleteUseCase postDeleteUseCase;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestBody PostCreateRequest request
    ) {
        Post post = postCreateUseCase.create(PostMapper.toDto(request));
        return ResponseEntity.ok(PostMapper.toResponse(post));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest request
    ) {
        Post post = postUpdateUseCase.update(PostMapper.toDto(request, postId));
        return ResponseEntity.ok(PostMapper.toResponse(post));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> readPost(
            @PathVariable Long postId
    ) {
        ResolvedPost resolvedPost = postReadUseCase.read(postId);
        if (resolvedPost == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(PostMapper.toDetailResponse(resolvedPost));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<PostResponse> deletePost(
            @PathVariable Long postId
    ) {
        postDeleteUseCase.delete(postId);
        return ResponseEntity.ok().build();
    }
}
