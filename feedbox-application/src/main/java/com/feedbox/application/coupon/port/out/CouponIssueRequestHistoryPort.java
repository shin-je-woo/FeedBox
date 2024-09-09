package com.feedbox.application.coupon.port.out;

public interface CouponIssueRequestHistoryPort {

    /**
     * 쿠폰 이벤트에 유저의 발급 요청이력이 없다면 기록
     */
    boolean addHistoryIfNotExists(Long couponEventId, Long userId);

    /**
     * 쿠폰 이벤트에 발급 요청을 몇 번째로 했는지 기록
     */
    Long getRequestSequentialNumber(Long couponEventId);
}
