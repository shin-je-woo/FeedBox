package com.feedbox.worker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbox.application.coupon.port.in.CouponIssueUseCase;
import com.feedbox.infrastructure.kafka.common.Topic;
import com.feedbox.infrastructure.kafka.message.CouponIssueRequestMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueWorker {

    private final ObjectMapper objectMapper;
    private final CouponIssueUseCase couponIssueUseCase;

    @KafkaListener(
            topics = {Topic.COUPON_ISSUE_REQUEST},
            groupId = "feedbox-coupon-issue",
            concurrency = "3"
    )
    public void listen(ConsumerRecord<String, String> record) {
        CouponIssueRequestMessage message;
        try {
            message = objectMapper.readValue(record.value(), CouponIssueRequestMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        sleep();
        couponIssueUseCase.save(message.getCouponEventId(), message.getUserId());
        log.info("쿠폰 발급 완료 {} 번 이벤트 {} 번 유저", message.getCouponEventId(), message.getUserId());
    }

    /**
     * DB 부하를 컨트롤하기 위해 0.2초 sleep
     */
    private void sleep() {
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
