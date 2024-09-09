package com.feedbox.domain.model.coupon;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Coupon {

    private Long id;
    private Long userId;
    private Long couponEventId;
    private LocalDateTime issuedAt;
    private LocalDateTime usedAt;

    public static Coupon of(Long userId, Long couponEventId) {
        return Coupon.builder()
                .userId(userId)
                .couponEventId(couponEventId)
                .issuedAt(LocalDateTime.now())
                .build();
    }

    public Coupon use() {
        usedAt = LocalDateTime.now();
        return this;
    }

    public boolean isUsed() {
        return usedAt != null;
    }
}
