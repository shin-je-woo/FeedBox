package com.feedbox.infrastructure.mysql.coupon.mapper;

import com.feedbox.domain.model.coupon.CouponEvent;
import com.feedbox.infrastructure.mysql.coupon.entity.CouponEventEntity;

public class CouponEventEntityMapper {

    public static CouponEvent toDomain(CouponEventEntity couponEventEntity) {
        return CouponEvent.builder()
                .id(couponEventEntity.getId())
                .displayName(couponEventEntity.getDisplayName())
                .expiresAt(couponEventEntity.getExpiresAt())
                .issueLimit(couponEventEntity.getIssueLimit())
                .build();
    }
}
