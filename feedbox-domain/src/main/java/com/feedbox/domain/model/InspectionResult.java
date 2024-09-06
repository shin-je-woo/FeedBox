package com.feedbox.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 검수 결과
 */
@Getter
@Builder
public class InspectionResult {
    private InspectionStatus status;
    private List<String> tags;

    /**
     * 검수 상태
     */
    public enum InspectionStatus {
        GOOD,
        BAD
    }

    public boolean isGoodStatus() {
        return status.equals(InspectionStatus.GOOD);
    }
}
