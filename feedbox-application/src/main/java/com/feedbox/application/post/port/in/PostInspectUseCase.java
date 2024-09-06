package com.feedbox.application.post.port.in;

import com.feedbox.domain.model.InspectedPost;
import com.feedbox.domain.model.Post;

public interface PostInspectUseCase {

    InspectedPost inspectAndGetIfValid(Post post);
}
