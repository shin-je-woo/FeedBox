package com.feedbox.application.coupon.port.in;

public interface CouponIssueHistoryUseCase {

    /**
     * 유저가 처음 발급 받는 요청인지 확인하고 없으면 발급
     */
    boolean issueCouponIfFirstRequest(Long couponEventId, Long userId);

    /**
     * 발급 가능한 쿠폰이 남았는지 확인
     */
    boolean hasRemainingCoupon(Long couponEventId);
}
