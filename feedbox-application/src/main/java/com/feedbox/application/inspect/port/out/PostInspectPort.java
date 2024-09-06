package com.feedbox.application.inspect.port.out;

import com.feedbox.domain.model.InspectionResult;
import com.feedbox.domain.model.Post;

public interface PostInspectPort {

    /**
     * 컨텐츠를 검수한다.
     */
    InspectionResult inspect(Post post, String categoryName);
}
