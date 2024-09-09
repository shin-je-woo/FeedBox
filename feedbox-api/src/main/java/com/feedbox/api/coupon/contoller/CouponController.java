package com.feedbox.api.coupon.contoller;

import com.feedbox.api.coupon.model.dto.request.CouponIssueRequest;
import com.feedbox.api.coupon.model.dto.response.CouponResponse;
import com.feedbox.api.coupon.model.mapper.ResolvedCouponMapper;
import com.feedbox.application.coupon.port.in.CouponIssueHistoryUseCase;
import com.feedbox.application.coupon.port.in.CouponIssueRequestUseCase;
import com.feedbox.application.coupon.port.in.CouponListUseCase;
import com.feedbox.domain.model.coupon.ResolvedCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final CouponIssueHistoryUseCase couponIssueHistoryUseCase;
    private final CouponIssueRequestUseCase couponIssueRequestUseCase;
    private final CouponListUseCase couponListUseCase;

    @PostMapping
    ResponseEntity<String> issue(
            @RequestBody CouponIssueRequest request
    ) {
        Long userId = request.getUserId();
        Long couponEventId = request.getCouponEventId();
        if (!couponIssueHistoryUseCase.issueCouponIfFirstRequest(couponEventId, userId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 쿠폰을 발행한 유저입니다.");
        }
        if (!couponIssueHistoryUseCase.hasRemainingCoupon(couponEventId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("쿠폰이 모두 소진되었습니다.");
        }
        couponIssueRequestUseCase.requestIssue(couponEventId, userId);
        return ResponseEntity.ok("쿠폰이 정상적으로 발급되었습니다.");
    }

    @GetMapping
    ResponseEntity<List<CouponResponse>> listUsableCoupons(
            @RequestParam Long userId
    ) {
        List<ResolvedCoupon> resolvedCoupons = couponListUseCase.listUsableCouponsByUserId(userId);
        return ResponseEntity.ok().body(
                resolvedCoupons.stream()
                        .map(ResolvedCouponMapper::toResponse)
                        .toList()
        );
    }
}
