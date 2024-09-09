package com.feedbox.application.coupon.service;

import com.feedbox.application.coupon.port.in.CouponIssueRequestUseCase;
import com.feedbox.application.coupon.port.out.CouponIssueRequestProducePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueRequestService implements CouponIssueRequestUseCase {

    private final CouponIssueRequestProducePort couponIssueRequestProducePort;

    @Override
    public void requestIssue(Long couponEventId, Long userId) {
        log.info("쿠폰발행 요청을 생성합니다. {} 번 이벤트, {} 번 유저", couponEventId, userId);
        couponIssueRequestProducePort.sendMessage(userId, couponEventId);
    }
}
