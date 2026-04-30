package com.springboot.easypay.dto;

import com.springboot.easypay.enums.PayrollStatus;

import java.math.BigDecimal;

public record TeamPayrollSummaryDto(
        Long employeeId,
        String employeeName,
        BigDecimal totalEarnings,
        PayrollStatus status
) {
}
