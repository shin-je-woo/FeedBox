package com.feedbox.application.coupon.service;

import com.feedbox.application.coupon.port.in.CouponIssueUseCase;
import com.feedbox.application.coupon.port.out.CouponPersistencePort;
import com.feedbox.domain.model.coupon.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponIssueService implements CouponIssueUseCase {

    private final CouponPersistencePort couponPersistencePort;

    @Override
    public Coupon save(Long couponEventId, Long userId) {
        Coupon coupon = Coupon.of(userId, couponEventId);
        return couponPersistencePort.save(coupon);
    }
}
