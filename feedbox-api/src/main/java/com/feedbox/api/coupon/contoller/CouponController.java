package com.feedbox.api.coupon.contoller;

import com.feedbox.api.coupon.model.dto.CouponIssueRequest;
import com.feedbox.application.coupon.port.in.CouponIssueHistoryUseCase;
import com.feedbox.application.coupon.port.in.CouponIssueRequestUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final CouponIssueHistoryUseCase couponIssueHistoryUseCase;
    private final CouponIssueRequestUseCase couponIssueRequestUseCase;

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
}
