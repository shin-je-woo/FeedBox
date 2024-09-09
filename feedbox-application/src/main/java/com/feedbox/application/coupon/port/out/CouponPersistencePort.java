package com.feedbox.application.coupon.port.out;

import com.feedbox.domain.model.coupon.Coupon;

public interface CouponPersistencePort {

    Coupon save(Coupon coupon);
}
