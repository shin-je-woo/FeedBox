package com.feedbox.infrastructure.mysql.coupon.adapter;

import com.feedbox.application.coupon.port.out.CouponEventPersistencePort;
import com.feedbox.domain.model.coupon.CouponEvent;
import com.feedbox.infrastructure.mysql.coupon.entity.CouponEventEntity;
import com.feedbox.infrastructure.mysql.coupon.mapper.CouponEventEntityMapper;
import com.feedbox.infrastructure.mysql.coupon.repository.CouponEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponEventPersistenceAdapter implements CouponEventPersistencePort {

    private final CouponEventRepository couponEventRepository;

    @Override
    public CouponEvent findById(long id) {
        CouponEventEntity couponEventEntity = couponEventRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        return CouponEventEntityMapper.toDomain(couponEventEntity);
    }
}
