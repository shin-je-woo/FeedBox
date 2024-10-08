package com.feedbox.api.coupon.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponIssueRequest {
    private Long userId;
    private Long couponEventId;
}
