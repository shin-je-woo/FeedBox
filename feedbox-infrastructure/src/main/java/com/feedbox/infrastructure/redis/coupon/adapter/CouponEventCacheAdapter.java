package com.feedbox.infrastructure.redis.coupon.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbox.application.coupon.port.out.CouponEventCachePort;
import com.feedbox.domain.model.coupon.CouponEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CouponEventCacheAdapter implements CouponEventCachePort {

    private static final String KEY_PREFIX = "coupon_event:";
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void set(CouponEvent couponEvent) {
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(couponEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        redisTemplate.opsForValue().set(
                generateCacheKey(couponEvent.getId()),
                jsonString,
                Duration.ofDays(7L)
        );
    }

    @Override
    public Optional<CouponEvent> get(Long couponEventId) {
        String jsonString = redisTemplate.opsForValue().get(generateCacheKey(couponEventId));
        if(!StringUtils.hasText(jsonString)) {
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(jsonString, CouponEvent.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateCacheKey(Long couponEventId) {
        return KEY_PREFIX + couponEventId;
    }
}
