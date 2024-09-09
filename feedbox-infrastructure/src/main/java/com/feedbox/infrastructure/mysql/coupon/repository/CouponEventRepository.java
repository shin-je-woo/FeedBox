package com.feedbox.infrastructure.mysql.coupon.repository;

import com.feedbox.infrastructure.mysql.coupon.entity.CouponEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponEventRepository extends JpaRepository<CouponEventEntity, Long> {
}
