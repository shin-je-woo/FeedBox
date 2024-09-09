package com.feedbox.application.coupon.port.out;

public interface CouponIssueRequestProducePort {

    void sendMessage(Long userId, Long couponEventId);
}
