package com.feedbox.application.subscribing.port.in;

import com.feedbox.domain.model.post.Post;

public interface SubscribingPostAddToInboxUseCase {

    void saveSubscribingInboxPost(Post post);
}
