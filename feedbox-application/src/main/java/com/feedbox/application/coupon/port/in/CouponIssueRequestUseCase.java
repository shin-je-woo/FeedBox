package com.feedbox.application.coupon.port.in;

public interface CouponIssueRequestUseCase {

    void requestIssue(Long couponEventId, Long userId);
}
