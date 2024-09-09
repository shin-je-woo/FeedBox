package com.feedbox.api.coupon.model.mapper;

import com.feedbox.api.coupon.model.dto.response.CouponResponse;
import com.feedbox.domain.model.coupon.ResolvedCoupon;

public class ResolvedCouponMapper {

    public static CouponResponse toResponse(ResolvedCoupon resolvedCoupon) {
        return CouponResponse.builder()
                .id(resolvedCoupon.getCoupon().getId())
                .displayName(resolvedCoupon.getCouponEvent().getDisplayName())
                .expiresAt(resolvedCoupon.getCouponEvent().getExpiresAt())
                .build();
    }
}
