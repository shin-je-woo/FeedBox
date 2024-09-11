package com.feedbox.infrastructure.userdata.client;

import com.feedbox.infrastructure.userdata.dto.CategoryResponse;
import com.feedbox.infrastructure.userdata.dto.UserResponse;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDataClient {

    private final WebClient userDataWebClient;
    private final AtomicInteger categoryCounter = new AtomicInteger();
    private final AtomicInteger userCounter = new AtomicInteger();

    @CircuitBreaker(name = "USER_DATA", fallbackMethod = "fallbackOnCategoryApi")
    public CategoryResponse getCategoryById(Long categoryId) {
        log.info("category 시도횟수 = {}", categoryCounter.incrementAndGet());
        return userDataWebClient.get()
                .uri("/categories/" + categoryId)
                .retrieve()
                .bodyToMono(CategoryResponse.class)
                .timeout(Duration.ofSeconds(3L))
                .block();
    }

    @CircuitBreaker(name = "USER_DATA", fallbackMethod = "fallbackOnUserApi")
    public UserResponse getUserById(Long userId) {
        log.info("user 시도횟수 = {}", userCounter.incrementAndGet());
        return userDataWebClient
                .get()
                .uri("/users/" + userId)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .timeout(Duration.ofSeconds(3L))
                .block();
    }

    @CircuitBreaker(name = "USER_DATA", fallbackMethod = "fallbackOnFollowerApi")
    public List<Long> getFollowerIdsByUserId(Long userId) {
        return userDataWebClient
                .get()
                .uri("/followers?followingId=" + userId)
                .retrieve()
                .bodyToFlux(Long.class)
                .timeout(Duration.ofSeconds(3L))
                .collectList()
                .block();
    }

    private CategoryResponse fallbackOnCategoryApi(Long categoryId, CallNotPermittedException e) {
        log.warn("Circuit Opened!! request categoryId = {}, {}", categoryId, e.getMessage());
        throw new RuntimeException("fail-fast!!");
    }

    private UserResponse fallbackOnUserApi(Long userId, CallNotPermittedException e) {
        log.warn("Circuit Opened!! request userId = {}, {}", userId, e.getMessage());
        throw new RuntimeException("fail-fast!!");
    }

    private List<Long> fallbackOnFollowerApi(Long userId, CallNotPermittedException e) {
        log.warn("Circuit Opened!! request userId = {}, {}", userId, e.getMessage());
        throw new RuntimeException("fail-fast!!");
    }
}
