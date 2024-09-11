package com.feedbox.infrastructure.userdata.client;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserDataCircuitBreakerConfig {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        return CircuitBreakerRegistry.ofDefaults();
    }

    @Bean
    public CircuitBreaker circuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10) // 슬라이딩 윈도우 사이즈
                .minimumNumberOfCalls(3) // 집계에 필요한 최소 호출 수
                .failureRateThreshold(30) // 실패 30% 이상 시 서킷 오픈
                .slowCallRateThreshold(30) // slowCallDurationThreshold 초과 비율이 30% 이상 시 서킷 오픈
                .slowCallDurationThreshold(Duration.ofSeconds(5L)) // 5초 이상 소요 시 실패로 간주
                .permittedNumberOfCallsInHalfOpenState(10) // HALF-OPEN -> CLOSE or OPEN 으로 판단하기 위해 호출 횟수
                .maxWaitDurationInHalfOpenState(Duration.ofMinutes(3L)) // HALF-OPEN 상태 최대 유지 시간
                .waitDurationInOpenState(Duration.ofMinutes(10L)) // OPEN -> HALF-OPEN 전환 전 기다리는 시간
                .automaticTransitionFromOpenToHalfOpenEnabled(true) // OPEN -> HALF-OPEN 으로 자동 전환 여부
                .build();
        return circuitBreakerRegistry.circuitBreaker("USER_DATA", circuitBreakerConfig);
    }
}
