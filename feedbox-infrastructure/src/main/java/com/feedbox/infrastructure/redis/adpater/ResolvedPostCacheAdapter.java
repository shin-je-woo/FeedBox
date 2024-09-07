package com.feedbox.infrastructure.redis.adpater;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbox.application.post.port.out.ResolvedPostCachePort;
import com.feedbox.domain.model.ResolvedPost;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ResolvedPostCacheAdapter implements ResolvedPostCachePort {

    private static final String KEY_PREFIX = "resolved_post:";
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void set(ResolvedPost resolvedPost) {
        if (resolvedPost == null) return;
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(resolvedPost);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        redisTemplate.opsForValue().set(
                generateKey(resolvedPost.getId()),
                jsonString,
                Duration.ofHours(2L)
        );
    }

    @Override
    public ResolvedPost get(Long postId) {
        String jsonString = redisTemplate.opsForValue().get(generateKey(postId));
        if (jsonString == null) return null;
        try {
            return objectMapper.readValue(jsonString, ResolvedPost.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateKey(Long postId) {
        return KEY_PREFIX + postId;
    }
}
