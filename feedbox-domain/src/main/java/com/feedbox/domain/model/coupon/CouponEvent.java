package com.feedbox.domain.model.coupon;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CouponEvent {

    private Long id;
    private String displayName;
    private LocalDateTime expiresAt; // 쿠폰 만료 일시
    private Long issueLimit; // 쿠폰 발급 제한 갯수

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isRemaining(long userOrder) {
        return issueLimit >= userOrder;
    }
}
