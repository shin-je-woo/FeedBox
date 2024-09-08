package com.feedbox.application.post.port.out;

import com.feedbox.domain.model.post.Post;

import java.util.List;

public interface PostPersistencePort {

    Post save(Post post);
    Post findById(Long id);
    List<Post> listByIds(List<Long> postIds);
}
