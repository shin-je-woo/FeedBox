package com.feedbox.application.post.port.out;

import com.feedbox.domain.model.Post;

public interface OriginalPostMessageProducePort {

    void sendCreateMessage(Post post);
    void sendUpdateMessage(Post post);
    void sendDeleteMessage(Post post);
}
