package com.feedbox.domain.model.coupon;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResolvedCoupon {

    private Coupon coupon;
    private CouponEvent couponEvent;

    public static ResolvedCoupon of(Coupon coupon, CouponEvent couponEvent) {
        return ResolvedCoupon.builder()
                .coupon(coupon)
                .couponEvent(couponEvent)
                .build();
    }

    public boolean canBeUsed() {
        return !couponEvent.isExpired() && !coupon.isUsed();
    }
}
