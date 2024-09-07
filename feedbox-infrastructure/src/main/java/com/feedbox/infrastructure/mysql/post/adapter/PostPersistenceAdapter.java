package com.feedbox.infrastructure.mysql.post.adapter;

import com.feedbox.application.post.port.out.PostPersistencePort;
import com.feedbox.domain.model.Post;
import com.feedbox.infrastructure.mysql.post.entity.PostEntity;
import com.feedbox.infrastructure.mysql.post.mapper.PostEntityMapper;
import com.feedbox.infrastructure.mysql.post.respoitory.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostPersistenceAdapter implements PostPersistencePort {

    private final PostJpaRepository postJpaRepository;

    @Override
    public Post save(Post post) {
        PostEntity postEntity = postJpaRepository.save(PostEntityMapper.toEntity(post));
        return PostEntityMapper.toDomain(postEntity);
    }

    @Override
    public Post findById(Long id) {
        PostEntity postEntity = postJpaRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        return PostEntityMapper.toDomain(postEntity);
    }

    @Override
    public List<Post> listByIds(List<Long> postIds) {
        return postJpaRepository.findAllById(postIds).stream()
                .map(PostEntityMapper::toDomain)
                .toList();
    }
}
