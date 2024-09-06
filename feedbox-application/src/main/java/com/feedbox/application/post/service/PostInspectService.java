package com.feedbox.application.post.service;

import com.feedbox.application.inspect.port.in.PostInspectUseCase;
import com.feedbox.application.inspect.port.out.PostInspectPort;
import com.feedbox.application.post.port.out.UserDataPort;
import com.feedbox.domain.model.InspectedPost;
import com.feedbox.domain.model.InspectionResult;
import com.feedbox.domain.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostInspectService implements PostInspectUseCase {

    private final UserDataPort userDataPort;
    private final PostInspectPort postInspectPort;

    @Override
    public InspectedPost inspectAndGetIfValid(Post post) {
        String categoryName = userDataPort.getCategoryNameByCategoryId(post.getCategoryId());
        InspectionResult inspectionResult = postInspectPort.inspect(post, categoryName);
        if (!inspectionResult.isGoodStatus()) return null;
        return InspectedPost.of(
                post,
                categoryName,
                inspectionResult.getTags(),
                LocalDateTime.now()
        );
    }
}
