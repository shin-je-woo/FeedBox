package com.feedbox.application.coupon.service;

import com.feedbox.application.coupon.port.in.CouponIssueRequestUseCase;
import com.feedbox.application.coupon.port.out.CouponIssueRequestProducePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponIssueRequestService implements CouponIssueRequestUseCase {

    private final CouponIssueRequestProducePort couponIssueRequestProducePort;

    @Override
    public void requestIssue(Long couponEventId, Long userId) {
        couponIssueRequestProducePort.sendMessage(userId, couponEventId);
    }
}
