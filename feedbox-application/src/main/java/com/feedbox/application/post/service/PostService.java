package com.feedbox.application.post.service;

import com.feedbox.application.post.port.in.PostCreateUseCase;
import com.feedbox.application.post.port.in.PostDeleteUseCase;
import com.feedbox.application.post.port.in.PostReadUseCase;
import com.feedbox.application.post.port.in.PostUpdateUseCase;
import com.feedbox.application.post.port.in.dto.PostCreateDto;
import com.feedbox.application.post.port.in.dto.PostUpdateDto;
import com.feedbox.domain.model.Post;
import com.feedbox.domain.model.ResolvedPost;
import org.springframework.stereotype.Service;

@Service
public class PostService implements PostCreateUseCase, PostReadUseCase, PostUpdateUseCase, PostDeleteUseCase {

    @Override
    public Post create(PostCreateDto postCreateDto) {
        // TODO Adapter 호출
        return Post.of(
                postCreateDto.getTitle(),
                postCreateDto.getContent(),
                postCreateDto.getUserId(),
                postCreateDto.getCategoryId()
        );
    }

    @Override
    public ResolvedPost read(Long id) {
        return null;
    }

    @Override
    public Post update(PostUpdateDto postUpdateDto) {
        return null;
    }

    @Override
    public void delete(Long postId) {
    }

}
