package com.feedbox.infrastructure.redis.coupon.adapter;

import com.feedbox.application.coupon.port.out.CouponIssueRequestHistoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CouponIssueRequestHistoryAdapter implements CouponIssueRequestHistoryPort {

    private static final String USER_REQUEST_HISTORY_KEY_PREFIX = "coupon_history.user_request:";
    private static final String REQUEST_COUNT_HISTORY_KEY_PREFIX = "coupon_history.request_count:";
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean addHistoryIfNotExists(Long couponEventId, Long userId) {
        String key = generateUserRequestHistoryKey(couponEventId, userId);
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(
                key,
                "empty",
                Duration.ofDays(7L)));
    }

    @Override
    public Long getRequestSequentialNumber(Long couponEventId) {
        String key = generateRequestCountHistoryKey(couponEventId);
        Long requestSequentialNumber = redisTemplate.opsForValue().increment(key, 1L);
        if (Objects.equals(requestSequentialNumber, 1L)) {
            // 처음 생성되면 TTL 설정
            redisTemplate.expire(key, Duration.ofDays(7L));
        }
        return requestSequentialNumber;
    }

    private String generateUserRequestHistoryKey(Long couponEventId, Long userId) {
        return USER_REQUEST_HISTORY_KEY_PREFIX + couponEventId + ":" + userId;
    }

    private String generateRequestCountHistoryKey(Long couponEventId) {
        return REQUEST_COUNT_HISTORY_KEY_PREFIX + couponEventId;
    }
}
