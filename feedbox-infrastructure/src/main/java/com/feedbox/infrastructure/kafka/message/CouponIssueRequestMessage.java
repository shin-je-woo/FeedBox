package com.feedbox.infrastructure.kafka.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponIssueRequestMessage {
    private Long userId;
    private Long couponEventId;

    public static CouponIssueRequestMessage of(Long userId, Long couponEventId) {
        return CouponIssueRequestMessage.builder()
                .userId(userId)
                .couponEventId(couponEventId)
                .build();
    }
}
