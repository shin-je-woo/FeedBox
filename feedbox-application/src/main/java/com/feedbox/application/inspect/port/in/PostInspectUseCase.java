package com.feedbox.application.inspect.port.in;

import com.feedbox.domain.model.post.InspectedPost;
import com.feedbox.domain.model.post.Post;

public interface PostInspectUseCase {

    /**
     * 컨텐츠를 검수하고, 검수 성공하면 컨텐츠를 반환한다.
     */
    InspectedPost inspectAndGetIfValid(Post post);
}
