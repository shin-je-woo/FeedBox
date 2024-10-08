package com.feedbox.application.search.port.in;

import com.feedbox.domain.model.post.ResolvedPost;

import java.util.List;

public interface PostSearchUseCase {

    List<ResolvedPost> getSearchResultByKeyword(String keyword, int pageNumber);
}
