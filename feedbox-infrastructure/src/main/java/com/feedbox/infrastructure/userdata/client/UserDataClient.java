package com.feedbox.infrastructure.userdata.client;

import com.feedbox.infrastructure.userdata.dto.CategoryResponse;
import com.feedbox.infrastructure.userdata.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDataClient {

    private final WebClient userDataWebClient;

    public CategoryResponse getCategoryById(Long categoryId) {
        return userDataWebClient.get()
                .uri("/categories/" + categoryId)
                .retrieve()
                .bodyToMono(CategoryResponse.class)
                .block();
    }

    public UserResponse getUserById(Long userId) {
        return userDataWebClient
                .get()
                .uri("/users/" + userId)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }

    public List<Long> getFollowerIdsByUserId(Long userId) {
        return userDataWebClient
                .get()
                .uri("/followers?followingId=" + userId)
                .retrieve()
                .bodyToFlux(Long.class)
                .collectList()
                .block();
    }
}
