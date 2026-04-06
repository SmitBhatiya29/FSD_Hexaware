package com.springboot.easypay.dto;

import com.springboot.easypay.enums.PayrollStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PayrollRecordResDto(
        Long id,
        Long employeeId,
        String employeeName,
        String payMonth,
        BigDecimal totalEarnings,
        BigDecimal totalDeductions,
        BigDecimal netPayable,
        PayrollStatus status,
        LocalDate processedDate
) {
}
