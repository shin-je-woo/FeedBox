package com.feedbox.application.coupon.port.out;

import com.feedbox.domain.model.coupon.CouponEvent;

/**
 * create, update, delete 등은 없음. 쿠폰발행은 이미 되어 있다고 가정(schema.sql, data.sql)
 */
public interface CouponEventPersistencePort {

    CouponEvent findById(long id);
}
