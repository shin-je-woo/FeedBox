package com.feedbox.application.coupon.port.out;

import com.feedbox.domain.model.coupon.Coupon;
import com.feedbox.domain.model.coupon.ResolvedCoupon;

import java.util.List;

public interface CouponPersistencePort {

    Coupon save(Coupon coupon);
    List<ResolvedCoupon> listByUserId(Long userId);
}
