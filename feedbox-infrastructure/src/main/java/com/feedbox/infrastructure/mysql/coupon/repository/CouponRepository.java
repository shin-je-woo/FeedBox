package com.feedbox.infrastructure.mysql.coupon.repository;

import com.feedbox.infrastructure.mysql.coupon.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
}
