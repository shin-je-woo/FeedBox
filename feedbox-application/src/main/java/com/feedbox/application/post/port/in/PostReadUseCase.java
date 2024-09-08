package com.feedbox.application.post.port.in;

import com.feedbox.domain.model.post.ResolvedPost;

public interface PostReadUseCase {

    ResolvedPost read(Long id);
}
