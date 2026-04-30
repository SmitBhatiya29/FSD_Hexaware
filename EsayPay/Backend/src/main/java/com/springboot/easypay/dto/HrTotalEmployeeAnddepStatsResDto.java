package com.springboot.easypay.dto;

import java.math.BigDecimal;
import java.util.List;

public record HrTotalEmployeeAnddepStatsResDto(
        List<StatsOFHrGroupByDto> data,
        Long totalEmployee,
        BigDecimal totalEstimatedPay
) {
}
