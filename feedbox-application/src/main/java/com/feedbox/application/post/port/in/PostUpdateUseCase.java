package com.feedbox.application.post.port.in;

import com.feedbox.application.post.port.in.dto.PostUpdateDto;
import com.feedbox.domain.model.Post;

public interface PostUpdateUseCase {

    Post update(PostUpdateDto postUpdateDto);
}
