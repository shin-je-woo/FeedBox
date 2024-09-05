package com.feedbox.api.post.controller;

import com.feedbox.application.post.port.out.TestChatGptPort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestChatGptPort testChatGptPort;

    @GetMapping("/test")
    public String test(
            @RequestParam String content
    ) {
        return testChatGptPort.test(content);
    }
}
