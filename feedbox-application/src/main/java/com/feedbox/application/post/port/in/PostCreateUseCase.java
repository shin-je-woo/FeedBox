package com.feedbox.application.post.port.in;

import com.feedbox.application.post.port.in.dto.PostCreateDto;
import com.feedbox.domain.model.Post;

public interface PostCreateUseCase {

    Post create(PostCreateDto postCreateDto);
}
