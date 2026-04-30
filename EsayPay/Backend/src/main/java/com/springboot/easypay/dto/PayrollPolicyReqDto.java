package com.springboot.easypay.dto;

import java.math.BigDecimal;

public record PayrollPolicyReqDto(
        BigDecimal minSalary,
        BigDecimal maxSalary,
        BigDecimal taxPercentage,
        BigDecimal hraPercentage,
        BigDecimal standardDeduction
) {
}
