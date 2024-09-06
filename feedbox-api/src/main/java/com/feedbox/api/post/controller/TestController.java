package com.feedbox.api.post.controller;

import com.feedbox.application.inspect.port.in.PostInspectUseCase;
import com.feedbox.domain.model.InspectedPost;
import com.feedbox.domain.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final PostInspectUseCase postInspectUseCase;

    @GetMapping("/test")
    public InspectedPost test(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam Long categoryId
    ) {
        return postInspectUseCase.inspectAndGetIfValid(
                Post.of(
                        title,
                        content,
                        categoryId,
                        categoryId
                )
        );
    }
}
