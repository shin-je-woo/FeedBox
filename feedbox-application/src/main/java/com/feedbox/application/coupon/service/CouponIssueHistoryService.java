package com.feedbox.application.coupon.service;

import com.feedbox.application.coupon.port.in.CouponIssueHistoryUseCase;
import com.feedbox.application.coupon.port.out.CouponEventCachePort;
import com.feedbox.application.coupon.port.out.CouponEventPersistencePort;
import com.feedbox.application.coupon.port.out.CouponIssueRequestHistoryPort;
import com.feedbox.domain.model.coupon.CouponEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponIssueHistoryService implements CouponIssueHistoryUseCase {

    private final CouponEventPersistencePort couponEventPersistencePort;
    private final CouponIssueRequestHistoryPort couponIssueRequestHistoryPort;
    private final CouponEventCachePort couponEventCachePort;

    @Override
    public boolean issueCouponIfFirstRequest(Long couponEventId, Long userId) {
        return couponIssueRequestHistoryPort.addHistoryIfNotExists(couponEventId, userId);
    }

    @Override
    public boolean hasRemainingCoupon(Long couponEventId) {
        CouponEvent couponEvent = getCouponEvent(couponEventId);
        Long userOrder = couponIssueRequestHistoryPort.getRequestSequentialNumber(couponEventId);
        return couponEvent.isRemaining(userOrder);
    }

    /**
     * Cache(Redis)에서 찾고, 없으면 Persistence(DB)에서 찾는다.
     */
    private CouponEvent getCouponEvent(Long couponEventId) {
        return couponEventCachePort.get(couponEventId)
                .orElseGet(() -> {
                    CouponEvent couponEvent = couponEventPersistencePort.findById(couponEventId);
                    couponEventCachePort.set(couponEvent);
                    return couponEvent;
                });
    }
}
