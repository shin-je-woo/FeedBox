package com.feedbox.application.post.service;

import com.feedbox.application.post.port.in.PostCreateUseCase;
import com.feedbox.application.post.port.in.PostDeleteUseCase;
import com.feedbox.application.post.port.in.PostReadUseCase;
import com.feedbox.application.post.port.in.PostUpdateUseCase;
import com.feedbox.application.post.port.in.dto.PostCreateDto;
import com.feedbox.application.post.port.in.dto.PostUpdateDto;
import com.feedbox.application.post.port.out.OriginalPostMessageProducePort;
import com.feedbox.application.post.port.out.PostPersistencePort;
import com.feedbox.domain.model.Post;
import com.feedbox.domain.model.ResolvedPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService implements PostCreateUseCase, PostReadUseCase, PostUpdateUseCase, PostDeleteUseCase {

    private final PostPersistencePort postPersistencePort;
    private final OriginalPostMessageProducePort originalPostMessageProducePort;

    /**
     * 콘텐츠를 생성한다.
     * 1) Mysql에 저장 2) Kafka 메시지 발행
     */
    @Transactional
    @Override
    public Post create(PostCreateDto postCreateDto) {
        Post post = Post.of(
                postCreateDto.getTitle(),
                postCreateDto.getContent(),
                postCreateDto.getUserId(),
                postCreateDto.getCategoryId()
        );
        Post savedPost = postPersistencePort.save(post);
        originalPostMessageProducePort.sendCreateMessage(savedPost);
        return savedPost;
    }

    @Override
    public ResolvedPost read(Long id) {
        return null;
    }

    /**
     * 콘텐츠를 수정한다.
     * 1) Mysql에 저장 2) Kafka 메시지 발행
     * 도메인 영역을 보호하지만, Layer를 나눴기 때문에 JPA의 장점인 변경감지를 사용하지 못한다.
     */
    @Transactional
    @Override
    public Post update(PostUpdateDto postUpdateDto) {
        Post post = postPersistencePort.findById(postUpdateDto.getPostId());
        post.update(
                postUpdateDto.getTitle(),
                postUpdateDto.getContent(),
                postUpdateDto.getCategoryId()
        );
        Post updatedPost = postPersistencePort.save(post);
        originalPostMessageProducePort.sendUpdateMessage(updatedPost);
        return updatedPost;
    }

    /**
     * 콘텐츠를 삭제한다.
     * 1) Mysql에 저장 2) Kafka 메시지 발행
     */
    @Override
    public void delete(Long postId) {
        Post post = postPersistencePort.findById(postId);
        if (post == null) return;
        post.delete();
        Post deletedPost = postPersistencePort.save(post);
        originalPostMessageProducePort.sendDeleteMessage(deletedPost);
    }
}
