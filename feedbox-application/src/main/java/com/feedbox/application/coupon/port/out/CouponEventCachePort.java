package com.feedbox.application.coupon.port.out;

import com.feedbox.domain.model.coupon.CouponEvent;

import java.util.Optional;

public interface CouponEventCachePort {

    void set(CouponEvent couponEvent);
    Optional<CouponEvent> get(Long couponEventId);
}
