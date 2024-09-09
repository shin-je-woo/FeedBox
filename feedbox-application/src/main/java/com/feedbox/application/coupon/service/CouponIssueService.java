package com.feedbox.application.coupon.service;

import com.feedbox.application.coupon.port.in.CouponIssueUseCase;
import com.feedbox.application.coupon.port.in.CouponListUseCase;
import com.feedbox.application.coupon.port.out.CouponPersistencePort;
import com.feedbox.domain.model.coupon.Coupon;
import com.feedbox.domain.model.coupon.ResolvedCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponIssueService implements CouponIssueUseCase, CouponListUseCase {

    private final CouponPersistencePort couponPersistencePort;

    @Override
    public Coupon save(Long couponEventId, Long userId) {
        Coupon coupon = Coupon.of(userId, couponEventId);
        return couponPersistencePort.save(coupon);
    }

    @Override
    public List<ResolvedCoupon> listUsableCouponsByUserId(Long userId) {
        return couponPersistencePort.listByUserId(userId).stream()
                .filter(ResolvedCoupon::canBeUsed)
                .toList();
    }
}
