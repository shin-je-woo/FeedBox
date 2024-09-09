package com.feedbox.infrastructure.mysql.coupon.adapter;

import com.feedbox.application.coupon.port.out.CouponPersistencePort;
import com.feedbox.domain.model.coupon.Coupon;
import com.feedbox.infrastructure.mysql.coupon.entity.CouponEntity;
import com.feedbox.infrastructure.mysql.coupon.mapper.CouponEntityMapper;
import com.feedbox.infrastructure.mysql.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponPersistenceAdapter implements CouponPersistencePort {

    private final CouponRepository couponRepository;

    @Override
    public Coupon save(Coupon coupon) {
        CouponEntity couponEntity = couponRepository.save(CouponEntityMapper.toEntity(coupon));
        return CouponEntityMapper.toDomain(couponEntity);
    }
}
