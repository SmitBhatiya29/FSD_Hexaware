package com.springboot.easypay.dto;

import java.math.BigDecimal;

public record StatsOFHrGroupByDto(
        String departmentName,
        Long totalEmployees

) {
}
