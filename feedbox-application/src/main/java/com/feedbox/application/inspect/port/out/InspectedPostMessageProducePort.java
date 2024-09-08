package com.feedbox.application.inspect.port.out;

import com.feedbox.domain.model.post.InspectedPost;

public interface InspectedPostMessageProducePort {

    void sendCreateMessage(InspectedPost inspectedPost);
    void sendUpdateMessage(InspectedPost inspectedPost);
    void sendDeleteMessage(Long postId);
}
