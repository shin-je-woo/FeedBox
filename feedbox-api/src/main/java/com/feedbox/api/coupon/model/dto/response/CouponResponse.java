package com.feedbox.api.coupon.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CouponResponse {
    private final Long id;
    private final String displayName;
    private final LocalDateTime expiresAt;
}
