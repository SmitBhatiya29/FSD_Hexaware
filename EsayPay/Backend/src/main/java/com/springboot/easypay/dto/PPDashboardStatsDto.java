package com.springboot.easypay.dto;

import java.math.BigDecimal;

public record PPDashboardStatsDto(
        long pendingBenefitRequests,
        BigDecimal totalBenefitPaid,
        BigDecimal totalEstimatedPay
) {
}
