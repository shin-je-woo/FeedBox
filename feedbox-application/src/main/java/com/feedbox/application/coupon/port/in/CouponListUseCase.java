package com.feedbox.application.coupon.port.in;

import com.feedbox.domain.model.coupon.ResolvedCoupon;

import java.util.List;

public interface CouponListUseCase {

    List<ResolvedCoupon> listUsableCouponsByUserId(Long userId);
}
