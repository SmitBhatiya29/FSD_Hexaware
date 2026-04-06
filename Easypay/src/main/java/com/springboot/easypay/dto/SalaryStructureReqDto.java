package com.springboot.easypay.dto;

import java.math.BigDecimal;

public record SalaryStructureReqDto(
        long employeeId,
        BigDecimal basicSalary,
        BigDecimal hraAllowance,
        BigDecimal standardDeduction,
        BigDecimal taxPercentage
        ) {
}
