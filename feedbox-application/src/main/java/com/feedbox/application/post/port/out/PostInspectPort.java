package com.feedbox.application.post.port.out;

import com.feedbox.domain.model.InspectionResult;
import com.feedbox.domain.model.Post;

public interface PostInspectPort {

    InspectionResult inspect(Post post, String categoryName);
}
