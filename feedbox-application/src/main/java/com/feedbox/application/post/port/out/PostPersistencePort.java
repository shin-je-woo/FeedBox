package com.feedbox.application.post.port.out;

import com.feedbox.domain.model.Post;

public interface PostPersistencePort {

    Post save(Post post);

    Post findById(Long id);

}
