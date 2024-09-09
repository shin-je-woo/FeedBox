package com.feedbox.infrastructure.mysql.coupon.mapper;

import com.feedbox.domain.model.coupon.Coupon;
import com.feedbox.infrastructure.mysql.coupon.entity.CouponEntity;

public class CouponEntityMapper {

    public static CouponEntity toEntity(Coupon coupon) {
        return CouponEntity.builder()
                .id(coupon.getId())
                .userId(coupon.getUserId())
                .couponEventId(coupon.getCouponEventId())
                .issuedAt(coupon.getIssuedAt())
                .usedAt(coupon.getUsedAt())
                .build();
    }

    public static Coupon toDomain(CouponEntity couponEntity) {
        return Coupon.builder()
                .id(couponEntity.getId())
                .userId(couponEntity.getUserId())
                .couponEventId(couponEntity.getCouponEventId())
                .issuedAt(couponEntity.getIssuedAt())
                .usedAt(couponEntity.getUsedAt())
                .build();
    }
}
