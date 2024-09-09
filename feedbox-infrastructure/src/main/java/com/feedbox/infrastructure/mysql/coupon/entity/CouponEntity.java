package com.feedbox.infrastructure.mysql.coupon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity(name = "coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @Column(name = "coupon_event_id")
    private Long couponEventId;
    private LocalDateTime issuedAt;
    private LocalDateTime usedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_event_id", nullable = false, insertable = false, updatable = false)
    private CouponEventEntity couponEventEntity;
}
