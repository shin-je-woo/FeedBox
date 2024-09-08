package com.feedbox.application.search.service;

import com.feedbox.application.post.port.in.PostResolvingUseCase;
import com.feedbox.application.search.port.in.PostIndexingUseCase;
import com.feedbox.application.search.port.in.PostSearchUseCase;
import com.feedbox.application.search.port.out.PostSearchPort;
import com.feedbox.domain.model.post.InspectedPost;
import com.feedbox.domain.model.post.ResolvedPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostSearchService implements PostIndexingUseCase, PostSearchUseCase {

    private static final int PAGE_SIZE = 5;
    private final PostSearchPort postSearchPort;
    private final PostResolvingUseCase postResolvingUseCase;

    @Override
    public void save(InspectedPost inspectedPost) {
        postSearchPort.indexPost(inspectedPost);
    }

    @Override
    public void delete(Long postId) {
        postSearchPort.deletePost(postId);
    }

    @Override
    public List<ResolvedPost> getSearchResultByKeyword(String keyword, int pageNumber) {
        List<Long> postIds = postSearchPort.searchPostIdsByKeyword(keyword, pageNumber, PAGE_SIZE);
        return postResolvingUseCase.resolvePosts(postIds);
    }
}
