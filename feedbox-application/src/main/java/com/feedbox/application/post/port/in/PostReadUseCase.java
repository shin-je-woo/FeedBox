package com.feedbox.application.post.port.in;

import com.feedbox.domain.model.ResolvedPost;

public interface PostReadUseCase {

    ResolvedPost read(Long id);
}
