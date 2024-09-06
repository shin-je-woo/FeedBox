package com.feedbox.application.post.port.out;

import com.feedbox.domain.model.InspectedPost;

public interface InspectedPostMessageProducePort {

    void sendCreateMessage(InspectedPost inspectedPost);
    void sendUpdateMessage(InspectedPost inspectedPost);
    void sendDeleteMessage(Long postId);
}
