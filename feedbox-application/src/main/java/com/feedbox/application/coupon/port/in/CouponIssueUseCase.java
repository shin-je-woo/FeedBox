package com.feedbox.application.coupon.port.in;

import com.feedbox.domain.model.coupon.Coupon;

public interface CouponIssueUseCase {

    Coupon save(Long couponEventId, Long userId);
}
